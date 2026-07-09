package com.szponty.recruitment_system.recruitmentprocess.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateProcessStepRequest(
        @NotNull
        UUID recruitmentProcessVersion,

        @NotBlank
        String name,
        String description,
        boolean requiresInterview,
        boolean requiresDepartmentApproval
) {
}
