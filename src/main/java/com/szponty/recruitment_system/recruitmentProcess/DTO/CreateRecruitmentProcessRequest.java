package com.szponty.recruitment_system.recruitmentProcess.DTO;

import jakarta.validation.constraints.NotBlank;

public record CreateRecruitmentProcessRequest(
        @NotBlank String name,
        String description
) {
}
