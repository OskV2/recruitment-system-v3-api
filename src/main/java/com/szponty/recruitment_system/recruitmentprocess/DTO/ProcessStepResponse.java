package com.szponty.recruitment_system.recruitmentprocess.DTO;

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
