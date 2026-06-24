package com.szponty.recruitment_system.recruitmentProcess.DTO;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ProcessStepResponse(
        UUID id,
        UUID recruitmentProcessVersion,
        String name,
        String description,
        boolean requiresInterview,
        boolean requiresDepartmentApproval,
        LocalDateTime createdAt
) {
}
