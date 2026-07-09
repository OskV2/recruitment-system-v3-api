package com.szponty.recruitment_system.recruitmentprocess.DTO;

import jakarta.validation.constraints.NotBlank;

public record CreateRecruitmentProcessRequest(
        @NotBlank String name,
        String description
) {
}
