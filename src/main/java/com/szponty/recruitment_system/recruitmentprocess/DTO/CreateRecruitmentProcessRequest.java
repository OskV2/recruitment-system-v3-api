package com.szponty.recruitment_system.recruitmentprocess.DTO;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record CreateRecruitmentProcessRequest(
        @NotBlank String name,
        String description
) {
}
