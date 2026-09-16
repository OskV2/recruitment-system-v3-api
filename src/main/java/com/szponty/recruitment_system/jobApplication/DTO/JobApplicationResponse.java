package com.szponty.recruitment_system.jobApplication.DTO;

import com.szponty.recruitment_system.jobApplication.model.JobApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record JobApplicationResponse(
    UUID id,
    UUID publicToken,
    UUID jobOfferId,
    UUID recruitmentProcessVersionId,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    JobApplicationStatus status,
    String githubLink,
    boolean deleted,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
