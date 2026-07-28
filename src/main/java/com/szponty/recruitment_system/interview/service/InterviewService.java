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

    // TODO: implement once JobApplicationStepRepository is available
    @Transactional(readOnly = true)
    public InterviewResponse getInterviewByJobApplicationStepId(UUID jobApplicationStepId) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    // TODO: implement once JobApplicationRepository is available
    @Transactional(readOnly = true)
    public List<InterviewResponse> getInterviewsForJobApplication(UUID jobApplicationId) {
        throw new UnsupportedOperationException("Not implemented yet.");
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
    public void deleteInterview(UUID uuid) {
        Interview interview = interviewRepository.getOrThrow(uuid, "Interview");

        if (interview.isDeleted()) {
            throw new InvalidEntityStateException(
                    "Interview " + uuid + " already deleted"
            );
        }

        interview.setDeleted(true);
    }

    @Transactional
    public void restoreInterview(UUID uuid) {
        Interview interview = interviewRepository.getOrThrow(uuid, "Interview");

        if (!interview.isDeleted()) {
            throw new InvalidEntityStateException(
                    "Interview " + uuid + " is not deleted"
            );
        }

        interview.setDeleted(false);
    }
}
