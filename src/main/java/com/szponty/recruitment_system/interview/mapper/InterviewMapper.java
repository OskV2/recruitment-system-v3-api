package com.szponty.recruitment_system.interview.mapper;

import com.szponty.recruitment_system.config.mapper.CentralMapperConfig;
import com.szponty.recruitment_system.interview.dto.InterviewResponse;
import com.szponty.recruitment_system.interview.model.Interview;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public abstract class InterviewMapper {

    public abstract InterviewResponse toResponse(Interview interview);

    public abstract List<InterviewResponse> toResponseList(List<Interview> interviews);
}
