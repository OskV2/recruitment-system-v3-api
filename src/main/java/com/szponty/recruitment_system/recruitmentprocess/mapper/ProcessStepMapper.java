package com.szponty.recruitment_system.recruitmentprocess.mapper;

import com.szponty.recruitment_system.config.mapper.CentralMapperConfig;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(config = CentralMapperConfig.class)
public interface ProcessStepMapper {
    @Mapping(target = "id", source = "step.id")
    @Mapping(target = "name", source = "step.name")
    @Mapping(target = "description", source = "step.description")
    @Mapping(target = "requiresInterview", source = "step.requiresInterview")
    @Mapping(target = "requiresDepartmentApproval", source = "step.requiresDepartmentApproval")
    @Mapping(target = "recruitmentProcessVersion", ignore = true)
    ProcessStepResponse toResponse(ProcessStep step);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "processStepRequest.name")
    @Mapping(target = "description", source = "processStepRequest.description")
    @Mapping(target = "requiresInterview", source = "processStepRequest.requiresInterview")
    @Mapping(target = "requiresDepartmentApproval", source = "processStepRequest.requiresDepartmentApproval")
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProcessStep toEntity(CreateProcessStepRequest processStepRequest);
}
