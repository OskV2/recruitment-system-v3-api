package com.szponty.recruitment_system.interview.DTO;

import com.szponty.recruitment_system.interview.model.InterviewStatus;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.user.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record InterviewResponse(
        UUID id,
        JobApplication jobApplication,
        User recruiter,
        LocalDateTime scheduledStart,
        LocalDateTime scheduledEnd,
        InterviewStatus status,
        String location,
        String meetingUrl,
        String notes,
        boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
