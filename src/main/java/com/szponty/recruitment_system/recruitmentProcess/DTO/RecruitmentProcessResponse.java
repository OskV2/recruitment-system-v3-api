package com.szponty.recruitment_system.recruitmentProcess.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruitmentProcessResponse(
        UUID id,
        String name,
        String description,
        Boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
