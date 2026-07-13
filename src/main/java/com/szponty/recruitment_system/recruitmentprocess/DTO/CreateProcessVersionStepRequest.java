package com.szponty.recruitment_system.recruitmentprocess.DTO;

import java.util.UUID;

public record CreateProcessVersionStepRequest(
        UUID processStepId,
        Integer stepOrder
) {}
