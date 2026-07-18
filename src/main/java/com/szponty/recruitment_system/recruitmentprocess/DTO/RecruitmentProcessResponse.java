package com.szponty.recruitment_system.recruitmentprocess.DTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RecruitmentProcessResponse(
        UUID id,
        String name,
        String description,
        Boolean deleted,
        Integer version,
        Boolean active,
        List<ProcessStepResponse> steps,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
