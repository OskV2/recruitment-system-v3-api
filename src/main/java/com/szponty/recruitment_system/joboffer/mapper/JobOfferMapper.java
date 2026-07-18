package com.szponty.recruitment_system.joboffer.mapper;

import com.szponty.recruitment_system.config.mapper.CentralMapperConfig;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.joboffer.dto.*;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;

import com.szponty.recruitment_system.user.model.User;

import java.time.LocalDateTime;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CentralMapperConfig.class)
public abstract class JobOfferMapper {
    @Mapping(target = "contractType", source = "contractType.name")
    @Mapping(target = "location", source = "location.city")
    @Mapping(target = "workModel", source = "workModel.name")
    @Mapping(target = "department", source = "department.name")
    public abstract AdminJobOfferShortResponse toAdminShortResponse(JobOffer jobOffer);

    @Mapping(target = "contractType", source = "contractType.name")
    @Mapping(target = "location", source = "location.city")
    @Mapping(target = "workModel", source = "workModel.name")
    @Mapping(target = "department", source = "department.name")
    public abstract AdminJobOfferDetailsResponse toAdminDetailsResponse(JobOffer jobOffer);

    @Mapping(target = "contractType", source = "contractType.name")
    @Mapping(target = "location", source = "location.city")
    @Mapping(target = "workModel", source = "workModel.name")
    public abstract PublicJobOfferShortResponse toPublicShortResponse(JobOffer jobOffer);

    @Mapping(target = "contractType", source = "contractType.name")
    @Mapping(target = "location", source = "location.city")
    @Mapping(target = "workModel", source = "workModel.name")
    public abstract PublicJobOfferDetailsResponse toPublicDetailsResponse(JobOffer jobOffer);

    public JobOffer toCreateEntity(CreateJobOfferRequest request,
                                   LocalDateTime validFrom,
                                   LocalDateTime validTo,
                                   ContractType contractType,
                                   Location location,
                                   FullTimeEquivalent fte,
                                   WorkModel workModel,
                                   Department department,
                                   RecruitmentProcessVersion recruitmentProcessVersion,
                                   User recruiter,
                                   User substituteRecruiter,
                                   Set<JobOfferBenefit> benefits,
                                   JobOfferStatus status
    ) {
        return JobOffer.builder()
                .name(request.name())
                .description(request.description())
                .salaryFrom(request.salaryFrom())
                .salaryTo(request.salaryTo())
                .currency(request.currency())
                .mustHaveRequirements(request.mustHaveRequirements())
                .niceToHaveRequirements(request.niceToHaveRequirements())
                .validFrom(validFrom)
                .validTo(validTo)
                .contractType(contractType)
                .location(location)
                .fullTimeEquivalent(fte)
                .workModel(workModel)
                .department(department)
                .recruitmentProcessVersion(recruitmentProcessVersion)
                .recruiterId(recruiter)
                .substituteRecruiterId(substituteRecruiter)
                .benefits(benefits)
                .offerStatus(status)
                .build();
    }

    public void toUpdateEntity(
            JobOffer jobOffer,
            CreateJobOfferRequest request,
            LocalDateTime validFrom,
            LocalDateTime validTo,
            ContractType contractType,
            Location location,
            FullTimeEquivalent fte,
            WorkModel workModel,
            Department department,
            RecruitmentProcessVersion recruitmentProcessVersion,
            User recruiter,
            User substituteRecruiter
    ) {
        if (request.name() != null) {
            jobOffer.setName(request.name());
        }

        if (request.description() != null) {
            jobOffer.setDescription(request.description());
        }

        if (request.salaryFrom() != null) {
            jobOffer.setSalaryFrom(request.salaryFrom());
        }

        if (request.salaryTo() != null) {
            jobOffer.setSalaryTo(request.salaryTo());
        }

        if (request.currency() != null) {
            jobOffer.setCurrency(request.currency());
        }

        if (request.mustHaveRequirements() != null) {
            jobOffer.setMustHaveRequirements(request.mustHaveRequirements());
        }

        if (request.niceToHaveRequirements() != null) {
            jobOffer.setNiceToHaveRequirements(request.niceToHaveRequirements());
        }

        if (validFrom != null) {
            jobOffer.setValidFrom(validFrom);
        }

        if (validTo != null) {
            jobOffer.setValidTo(validTo);
        }

        if (request.vacancy() != null) {
            jobOffer.setVacancy(request.vacancy());
        }

        if (contractType != null) {
            jobOffer.setContractType(contractType);
        }

        if (location != null) {
            jobOffer.setLocation(location);
        }

        if (fte != null) {
            jobOffer.setFullTimeEquivalent(fte);
        }

        if (workModel != null) {
            jobOffer.setWorkModel(workModel);
        }

        if (department != null) {
            jobOffer.setDepartment(department);
        }

        if (recruitmentProcessVersion != null) {
            jobOffer.setRecruitmentProcessVersion(recruitmentProcessVersion);
        }

        if (recruiter != null) {
            jobOffer.setRecruiterId(recruiter);
        }

        if (substituteRecruiter != null) {
            jobOffer.setSubstituteRecruiterId(substituteRecruiter);
        }
    }
}