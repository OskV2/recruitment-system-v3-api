package com.szponty.recruitment_system.recruitmentprocess.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruitmentProcessVersionResponse(
        UUID id,
        Integer version,
        boolean active,
        LocalDateTime createdAt
) {
}
