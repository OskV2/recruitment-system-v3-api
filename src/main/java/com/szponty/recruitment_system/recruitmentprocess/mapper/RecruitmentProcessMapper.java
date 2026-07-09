package com.szponty.recruitment_system.recruitmentprocess.mapper;

import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import org.springframework.stereotype.Component;

@Component
public class RecruitmentProcessMapper {
    public RecruitmentProcessResponse toResponse(RecruitmentProcess process) {
        return new RecruitmentProcessResponse(
                process.getId(),
                process.getName(),
                process.getDescription(),
                process.isDeleted(),
                process.getCreatedAt(),
                process.getUpdatedAt()
        );
    }

    public RecruitmentProcess toEntity(CreateRecruitmentProcessRequest request) {
        return RecruitmentProcess.builder()
                .name(request.name())
                .description(request.description())
                .build();
    }
}
