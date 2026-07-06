package com.szponty.recruitment_system.joboffer.mapper;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.joboffer.dto.*;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.user.dto.CreateUserRequest;
import com.szponty.recruitment_system.user.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class JobOfferMapper {
    public AdminJobOfferDetailsResponse toAdminDetailsResponse(JobOffer jobOffer) {
        return new AdminJobOfferDetailsResponse(
                jobOffer.getName(),
                jobOffer.getDescription(),
                jobOffer.getSalaryFrom(),
                jobOffer.getSalaryTo(),
                jobOffer.getCurrency(),
                jobOffer.getMustHaveRequirements(),
                jobOffer.getNiceToHaveRequirements(),
                jobOffer.getValidFrom(),
                jobOffer.getValidTo(),
                jobOffer.getContractType(),
                jobOffer.getLocation(),
                jobOffer.getFullTimeEquivalent(),
                jobOffer.getWorkModel(),
                jobOffer.getDepartment(),
                jobOffer.getRecruitmentProcessVersion(),
                jobOffer.getRecruiterId(),
                jobOffer.getSubstituteRecruiterId(),
                jobOffer.getBenefits(),
                jobOffer.getOfferStatus(),
                jobOffer.getVacancy()
        );
    }

    public AdminJobOfferShortResponse toAdminShortResponse(JobOffer jobOffer) {
        return new AdminJobOfferShortResponse(
                jobOffer.getName(),
                jobOffer.getDescription(),
                jobOffer.getSalaryFrom(),
                jobOffer.getSalaryTo(),
                jobOffer.getCurrency(),
                jobOffer.getValidFrom(),
                jobOffer.getValidTo(),
                jobOffer.getRecruiterId(),
                jobOffer.getOfferStatus(),
                jobOffer.getVacancy()
        );
    }

    public PublicJobOfferDetailsResponse toPublicDetailsResponse(JobOffer jobOffer) {
        return new PublicJobOfferDetailsResponse(
                jobOffer.getName(),
                jobOffer.getDescription(),
                jobOffer.getSalaryFrom(),
                jobOffer.getSalaryTo(),
                jobOffer.getCurrency(),
                jobOffer.getMustHaveRequirements(),
                jobOffer.getNiceToHaveRequirements(),
                jobOffer.getValidFrom(),
                jobOffer.getValidTo(),
                jobOffer.getContractType(),
                jobOffer.getLocation(),
                jobOffer.getFullTimeEquivalent(),
                jobOffer.getWorkModel(),
                jobOffer.getDepartment(),
                jobOffer.getRecruitmentProcessVersion(),
                jobOffer.getBenefits(),
                jobOffer.getVacancy()
        );
    }

    public PublicJobOfferShortResponse toPublicShortResponse(JobOffer jobOffer) {
        return new PublicJobOfferShortResponse(
                jobOffer.getName(),
                jobOffer.getSalaryFrom(),
                jobOffer.getSalaryTo(),
                jobOffer.getCurrency(),
                jobOffer.getValidTo(),
                jobOffer.getContractType(),
                jobOffer.getLocation(),
                jobOffer.getFullTimeEquivalent(),
                jobOffer.getWorkModel(),
                jobOffer.getDepartment(),
                jobOffer.getVacancy()
        );
    }

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
