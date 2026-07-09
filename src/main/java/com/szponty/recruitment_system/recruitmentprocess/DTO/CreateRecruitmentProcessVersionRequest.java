package com.szponty.recruitment_system.recruitmentprocess.DTO;

import java.util.List;
import java.util.UUID;

public record CreateRecruitmentProcessVersionRequest(
        List<UUID> stepIds
) { }
