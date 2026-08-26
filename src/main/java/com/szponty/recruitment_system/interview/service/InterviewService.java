package com.szponty.recruitment_system.interview.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.interview.DTO.CreateInterviewRequest;
import com.szponty.recruitment_system.interview.DTO.InterviewResponse;
import com.szponty.recruitment_system.interview.DTO.UpdateInterviewRequest;
import com.szponty.recruitment_system.interview.event.InterviewRescheduledEvent;
import com.szponty.recruitment_system.interview.mapper.InterviewMapper;
import com.szponty.recruitment_system.interview.model.Interview;
import com.szponty.recruitment_system.interview.model.InterviewStatus;
import com.szponty.recruitment_system.interview.repository.InterviewRepository;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStep;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStepStatus;
import com.szponty.recruitment_system.jobApplication.repository.JobApplicationStepRepository;
import com.szponty.recruitment_system.user.model.User;
import com.szponty.recruitment_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final JobApplicationStepRepository jobApplicationStepRepository;
    private final InterviewMapper interviewMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public InterviewResponse getInterviewById(UUID id) {
        Interview interview = interviewRepository.getOrThrow(id, "Interview");
        return interviewMapper.toResponse(interview);
    }

    @Transactional(readOnly = true)
    public InterviewResponse getInterviewByIdAndJobApplicationId(UUID jobApplicationId, UUID interviewId) {
        Interview interview = interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId)
                .orElseThrow(() -> new InvalidEntityStateException(
                        "Interview " + interviewId +
                        " does not belong to job application " + jobApplicationId
                ));

        return interviewMapper.toResponse(interview);
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsForJobApplication(UUID jobApplicationId) {
        List<Interview> interviews = interviewRepository.findByJobApplicationId(jobApplicationId);
        return interviews.stream()
                .map(interviewMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InterviewResponse> getAssignedInterviews(UUID recruiterId, Optional<InterviewStatus> interviewStatus) {
        userRepository.findById(recruiterId).orElseThrow(() -> new InvalidEntityStateException(
                "User " + recruiterId + " does not exist"
        ));

        List<Interview> interviews = interviewRepository.findByRecruiterIdAndOptionalStatus(
                recruiterId,
                interviewStatus.orElse(null)
        );

        return interviewMapper.toResponseList(interviews);
    }

    @Transactional
    public InterviewResponse createInterview(UUID jobApplicationStepId, CreateInterviewRequest request) {
        JobApplicationStep step = jobApplicationStepRepository.getOrThrow(jobApplicationStepId, "JobApplicationStep");

        // Can't create interview, because step is either in progress or already finished
        if (step.getStatus() != JobApplicationStepStatus.WAITING) {
            throw new InvalidEntityStateException("Cannot create interview for step " + step.getId());
        }

        if (interviewRepository.existsByJobApplicationStepId(jobApplicationStepId)) {
            throw new InvalidEntityStateException(
                    "Interview already exists for job application step " + jobApplicationStepId
            );
        }

        User recruiter = userRepository.getOrThrow(request.recruiterId(), "User");

        if (!request.scheduledStart().isBefore(request.scheduledEnd())) {
            throw new InvalidEntityStateException(
                    "Interview start time must be before end time"
            );
        }

        Interview interview = Interview.builder()
                .jobApplication(step.getJobApplication())
                .jobApplicationStep(step)
                .recruiter(recruiter)
                .scheduledStart(request.scheduledStart())
                .scheduledEnd(request.scheduledEnd())
                .location(request.location())
                .meetingUrl(request.meetingUrl())
                .notes(request.notes())
                .status(InterviewStatus.SCHEDULED)
                .build();

        Interview savedInterview = interviewRepository.save(interview);
        return interviewMapper.toResponse(savedInterview);
    }

    @Transactional
    public InterviewResponse updateInterviewDetails(UUID jobApplicationId, UUID interviewId, UpdateInterviewRequest request) {
        Interview interview = interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId)
                .orElseThrow(() -> new InvalidEntityStateException(
                        "Interview " + interviewId +
                                " does not belong to job application " + jobApplicationId
                ));

        LocalDateTime newStart = request.scheduledStart() != null
                ? request.scheduledStart()
                : interview.getScheduledStart();

        LocalDateTime newEnd = request.scheduledEnd() != null
                ? request.scheduledEnd()
                : interview.getScheduledEnd();

        if (newStart.isAfter(newEnd)) {
            throw new InvalidEntityStateException(
                    "Interview start time cannot be after end time"
            );
        }

        LocalDateTime oldStart = interview.getScheduledStart();
        LocalDateTime oldEnd = interview.getScheduledEnd();
        String oldLocation = interview.getLocation();
        String oldMeetingUrl = interview.getMeetingUrl();


        if (request.recruiterId() != null) {
            User recruiter = userRepository.getOrThrow(request.recruiterId(), "User");
            interview.setRecruiter(recruiter);
        }

        if (request.scheduledStart() != null) {
            interview.setScheduledStart(request.scheduledStart());
        }

        if (request.scheduledEnd() != null) {
            interview.setScheduledEnd(request.scheduledEnd());
        }

        if (request.location() != null) {
            interview.setLocation(request.location());
        }

        if (request.meetingUrl() != null) {
            interview.setMeetingUrl(request.meetingUrl());
        }

        if (request.status() != null) {
            interview.setStatus(request.status());
        }

        boolean candidateRelevantChange =
                !oldStart.equals(interview.getScheduledStart())
                        || !oldEnd.equals(interview.getScheduledEnd())
                        || !java.util.Objects.equals(oldLocation, interview.getLocation())
                        || !java.util.Objects.equals(oldMeetingUrl, interview.getMeetingUrl());

        if (candidateRelevantChange) {
            eventPublisher.publishEvent(new InterviewRescheduledEvent(
                    interview.getId(),
                    jobApplicationId,
                    oldStart, interview.getScheduledStart(),
                    oldEnd, interview.getScheduledEnd(),
                    oldLocation, interview.getLocation(),
                    oldMeetingUrl, interview.getMeetingUrl()
            ));
        }

        return interviewMapper.toResponse(interview);
    }

    @Transactional
    public void deleteInterview(UUID jobApplicationId, UUID interviewId) {
        Interview interview = interviewRepository
                .findByIdAndJobApplicationId(interviewId, jobApplicationId)
                .orElseThrow(() -> new InvalidEntityStateException(
                        "Interview " + interviewId +
                                " does not belong to job application " + jobApplicationId
                ));

        if (interview.isDeleted()) {
            throw new InvalidEntityStateException(
                    "Interview " + interviewId + " already deleted"
            );
        }

        interview.setDeleted(true);
    }

    @Transactional
    public void restoreInterview(UUID jobApplicationId, UUID interviewId) {
        Interview interview = interviewRepository
                .findByIdAndJobApplicationId(interviewId, jobApplicationId)
                .orElseThrow(() -> new InvalidEntityStateException(
                        "Interview " + interviewId +
                                " does not belong to job application " + jobApplicationId
                ));

        if (!interview.isDeleted()) {
            throw new InvalidEntityStateException(
                    "Interview " + interviewId + " is not deleted"
            );
        }

        interview.setDeleted(false);
    }
}
