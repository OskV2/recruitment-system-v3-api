package com.szponty.recruitment_system.recruitmentProcess.DTO;

import java.util.UUID;

public record UpdateProcessStepRequest (
        UUID recruitmentProcessVersion,
        String name,
        String description,
        Boolean requiresInterview,
        Boolean requiresDepartmentApproval
) {
}
