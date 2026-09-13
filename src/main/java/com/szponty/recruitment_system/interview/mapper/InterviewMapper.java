package com.szponty.recruitment_system.interview.mapper;

import com.szponty.recruitment_system.config.mapper.CentralMapperConfig;
import com.szponty.recruitment_system.interview.DTO.InterviewJobApplicationResponse;
import com.szponty.recruitment_system.interview.DTO.InterviewRecruiterResponse;
import com.szponty.recruitment_system.interview.DTO.InterviewResponse;
import com.szponty.recruitment_system.interview.model.Interview;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public abstract class InterviewMapper {

    public abstract InterviewResponse toResponse(Interview interview);

    public abstract List<InterviewResponse> toResponseList(List<Interview> interviews);

    public abstract InterviewRecruiterResponse toRecruiterResponse(User user);

    @Mapping(target = "jobOfferName", source = "jobOffer.name")
    public abstract InterviewJobApplicationResponse toJobApplicationResponse(JobApplication jobApplication);
}
