package com.szponty.recruitment_system.recruitmentprocess.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record UpdateRecruitmentProcessRequest(
        String name,
        String description,
        @NotEmpty @Valid List<UUID> processSteps
) {}
