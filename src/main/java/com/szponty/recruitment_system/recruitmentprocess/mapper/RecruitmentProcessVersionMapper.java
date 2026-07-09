package com.szponty.recruitment_system.recruitmentprocess.mapper;

import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
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
