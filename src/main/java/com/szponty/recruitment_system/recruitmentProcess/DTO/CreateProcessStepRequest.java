package com.szponty.recruitment_system.recruitmentProcess.DTO;

import java.util.UUID;

public record CreateProcessStepRequest(
        UUID recruitmentProcessVersion,
        String name,
        String description,
        boolean requiresInterview,
        boolean requiresDepartmentApproval
) {
}
