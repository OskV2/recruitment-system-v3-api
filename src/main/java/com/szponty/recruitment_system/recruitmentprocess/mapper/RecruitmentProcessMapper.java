package com.szponty.recruitment_system.recruitmentprocess.mapper;

import com.szponty.recruitment_system.config.mapper.CentralMapperConfig;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessVersionStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;


@Mapper(config = CentralMapperConfig.class)
public interface RecruitmentProcessMapper {

    @Mapping(target = "id", source = "recruitmentProcess.id")
    @Mapping(target = "name", source = "recruitmentProcess.name")
    @Mapping(target = "description", source = "recruitmentProcess.description")
    @Mapping(target = "deleted", source = "recruitmentProcess.deleted")
    @Mapping(target = "createdAt", source = "recruitmentProcess.createdAt")
    @Mapping(target = "updatedAt", source = "recruitmentProcess.updatedAt")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "steps", source = "steps", qualifiedByName = "mapSortedSteps")
    RecruitmentProcessResponse toResponse(RecruitmentProcessVersion version);

    @Mapping(target = "id", source = "processStep.id")
    @Mapping(target = "recruitmentProcessVersion", source = "recruitmentProcessVersion.id")
    @Mapping(target = "name", source = "processStep.name")
    @Mapping(target = "description", source = "processStep.description")
    @Mapping(target = "requiresInterview", source = "processStep.requiresInterview")
    @Mapping(target = "requiresDepartmentApproval", source = "processStep.requiresDepartmentApproval")
    @Mapping(target = "createdAt", source = "processStep.createdAt")
    ProcessStepResponse toStepResponse(ProcessVersionStep pvs);

    @org.mapstruct.Named("mapSortedSteps")
    default List<ProcessStepResponse> mapSortedSteps(List<ProcessVersionStep> steps) {
        return steps.stream()
                .sorted(Comparator.comparingInt(ProcessVersionStep::getStepOrder))
                .map(this::toStepResponse)
                .toList();
    }
}