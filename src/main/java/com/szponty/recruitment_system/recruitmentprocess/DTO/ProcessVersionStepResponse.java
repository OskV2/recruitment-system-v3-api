package com.szponty.recruitment_system.recruitmentprocess.DTO;

import java.util.UUID;

public record ProcessVersionStepResponse(
        UUID id,
        Integer stepOrder,
        ProcessStepResponse step
) {}
