package com.szponty.recruitment_system.recruitmentProcess.mapper;

import com.szponty.recruitment_system.recruitmentProcess.DTO.CreateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentProcess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentProcess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import org.springframework.stereotype.Component;

@Component
public class ProcessStepMapper {
    public ProcessStepResponse toResponse(ProcessStep step) {
        return ProcessStepResponse.builder()
                .id(step.getId())
                .recruitmentProcessVersion(step.getProcessVersion().getId())
                .name(step.getName())
                .description(step.getDescription())
                .requiresInterview(step.isRequiresInterview())
                .requiresDepartmentApproval(step.isRequiresDepartmentApproval())
                .build();
    }

    public ProcessStep toEntity(CreateProcessStepRequest processStepRequest, RecruitmentProcessVersion recruitmentProcessVersion) {
        return ProcessStep.builder()
                .processVersion(recruitmentProcessVersion)
                .name(processStepRequest.name())
                .description(processStepRequest.description())
                .requiresInterview(processStepRequest.requiresInterview())
                .requiresDepartmentApproval(processStepRequest.requiresDepartmentApproval())
                .build();
    }
}
