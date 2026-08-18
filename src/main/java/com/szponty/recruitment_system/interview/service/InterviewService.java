package com.szponty.recruitment_system.interview.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.interview.dto.InterviewResponse;
import com.szponty.recruitment_system.interview.mapper.InterviewMapper;
import com.szponty.recruitment_system.interview.model.Interview;
import com.szponty.recruitment_system.interview.model.InterviewStatus;
import com.szponty.recruitment_system.interview.repository.InterviewRepository;
import com.szponty.recruitment_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final InterviewMapper interviewMapper;

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
