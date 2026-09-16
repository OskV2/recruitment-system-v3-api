package com.szponty.recruitment_system.jobApplication.mapper;

import com.szponty.recruitment_system.config.mapper.CentralMapperConfig;
import com.szponty.recruitment_system.jobApplication.DTO.JobApplicationResponse;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public abstract class JobApplicationMapper {

    @Mapping(target = "jobOfferId", source = "jobOffer.id")
    @Mapping(target = "recruitmentProcessVersionId", source = "recruitmentProcessVersion.id")
    public abstract JobApplicationResponse toResponse(JobApplication jobApplication);
}
