package com.szponty.recruitment_system.recruitmentProcess.mapper;

import com.szponty.recruitment_system.recruitmentProcess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentProcessVersionMapper {
    public RecruitmentProcessVersionResponse toResponse(RecruitmentProcessVersion recruitmentProcessVersion) {
        return new RecruitmentProcessVersionResponse(
                recruitmentProcessVersion.getId(),
                recruitmentProcessVersion.getVersion(),
                recruitmentProcessVersion.isActive(),
                recruitmentProcessVersion.getCreatedAt()
        );
    }
}
