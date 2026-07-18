package com.szponty.recruitment_system.joboffer.service;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.joboffer.dto.AdminJobOfferDetailsResponse;
import com.szponty.recruitment_system.joboffer.dto.AdminJobOfferShortResponse;
import com.szponty.recruitment_system.joboffer.dto.ChangeJobOfferStatusRequest;
import com.szponty.recruitment_system.joboffer.dto.CreateJobOfferRequest;
import com.szponty.recruitment_system.joboffer.mapper.JobOfferMapper;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.joboffer.repository.JobOfferRepository;
import com.szponty.recruitment_system.joboffer.resolver.JobOfferBenefitResolver;
import com.szponty.recruitment_system.joboffer.resolver.JobOfferReferenceResolver;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminJobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;
    private final JobOfferReferenceResolver referenceResolver;
    private final JobOfferBenefitResolver benefitResolver;

    @Transactional(readOnly = true)
    public List<AdminJobOfferShortResponse> getAll() {
        return jobOfferRepository.findAll()
                .stream()
                .map(jobOfferMapper::toAdminShortResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminJobOfferDetailsResponse get(UUID id) {
        JobOffer jobOffer = getJobOfferOrThrow(id);

        return jobOfferMapper.toAdminDetailsResponse(jobOffer);
    }

    @Transactional
    public AdminJobOfferDetailsResponse create(CreateJobOfferRequest request) {
        JobOfferStatus status = request.offerStatus();

        validateCreateStatus(status);

        ContractType contractType = referenceResolver.resolveContractType(request.contractTypeId());
        Location location = referenceResolver.resolveLocation(request.locationId());
        FullTimeEquivalent fte = referenceResolver.resolveFullTimeEquivalent(request.fullTimeEquivalentId());
        WorkModel workModel = referenceResolver.resolveWorkModel(request.workModelId());
        Department department = referenceResolver.resolveDepartment(request.departmentId());
        RecruitmentProcessVersion recruitmentProcessVersion = referenceResolver.resolveRecruitmentProcessVersion(request.recruitmentProcessVersionId());
        User recruiter = referenceResolver.resolveUser(request.recruiterId());
        User substituteRecruiter = referenceResolver.resolveUser(request.substituteRecruiterId());

        LocalDateTime validFrom = request.validFrom();
        LocalDateTime validTo = request.validTo();

        JobOffer jobOffer = jobOfferMapper.toCreateEntity(
                request,
                validFrom,
                validTo,
                contractType,
                location,
                fte,
                workModel,
                department,
                recruitmentProcessVersion,
                recruiter,
                substituteRecruiter,
                Set.of(),
                status
        );

        JobOffer savedJobOffer = jobOfferRepository.save(jobOffer);

        Set<JobOfferBenefit> benefits = benefitResolver.createBenefits(savedJobOffer, request.benefits());
        savedJobOffer.setBenefits(benefits);

        return jobOfferMapper.toAdminDetailsResponse(savedJobOffer);
    }

    @Transactional
    public AdminJobOfferDetailsResponse update(UUID id, CreateJobOfferRequest request) {
        JobOffer jobOffer = getJobOfferOrThrow(id);

        if (jobOffer.getOfferStatus() != JobOfferStatus.DRAFT) {
            throw new IllegalArgumentException("Only draft job offers can be edited");
        }

        ContractType contractType = request.contractTypeId() != null
                ? referenceResolver.resolveContractType(request.contractTypeId())
                : null;

        Location location = request.locationId() != null
                ? referenceResolver.resolveLocation(request.locationId())
                : null;

        FullTimeEquivalent fte = request.fullTimeEquivalentId() != null
                ? referenceResolver.resolveFullTimeEquivalent(request.fullTimeEquivalentId())
                : null;

        WorkModel workModel = request.workModelId() != null
                ? referenceResolver.resolveWorkModel(request.workModelId())
                : null;

        Department department = request.departmentId() != null
                ? referenceResolver.resolveDepartment(request.departmentId())
                : null;

        RecruitmentProcessVersion recruitmentProcessVersion = request.recruitmentProcessVersionId() != null
                ? referenceResolver.resolveRecruitmentProcessVersion(request.recruitmentProcessVersionId())
                : null;

        User recruiter = request.recruiterId() != null
                ? referenceResolver.resolveUser(request.recruiterId())
                : null;

        User substituteRecruiter = request.substituteRecruiterId() != null
                ? referenceResolver.resolveUser(request.substituteRecruiterId())
                : null;

        LocalDateTime validFrom = request.validFrom();
        LocalDateTime validTo = request.validTo();

        JobOfferStatus status = request.offerStatus();

        if (status != null) {
            validateCreateStatus(status);
        }

        jobOfferMapper.toUpdateEntity(
                jobOffer,
                request,
                validFrom,
                validTo,
                contractType,
                location,
                fte,
                workModel,
                department,
                recruitmentProcessVersion,
                recruiter,
                substituteRecruiter
        );

        if (request.benefits() != null) {
            benefitResolver.updateBenefits(jobOffer, request.benefits());
        }

        return jobOfferMapper.toAdminDetailsResponse(jobOffer);
    }

    @Transactional
    public AdminJobOfferDetailsResponse changeStatus(UUID id, ChangeJobOfferStatusRequest request) {
        JobOffer jobOffer = getJobOfferOrThrow(id);

        JobOfferStatus newStatus = request.offerStatus();

        if (newStatus == JobOfferStatus.PENDING || newStatus == JobOfferStatus.ACTIVE) {
            validateReadyToPublish(jobOffer);
        }

        jobOffer.setOfferStatus(newStatus);

        return jobOfferMapper.toAdminDetailsResponse(jobOffer);
    }

    private JobOffer getJobOfferOrThrow(UUID id) {
        return jobOfferRepository.getOrThrow(id, "Job offer");
    }

    private void validateCreateStatus(JobOfferStatus status) {
        if (
                status != JobOfferStatus.DRAFT &&
                        status != JobOfferStatus.PENDING &&
                        status != JobOfferStatus.ACTIVE
        ) {
            throw new IllegalArgumentException("Invalid status for creating job offer");
        }
    }

    private void validateReadyToPublish(JobOffer jobOffer) {
        if (
                jobOffer.getName() == null ||
                        jobOffer.getDescription() == null ||
                        jobOffer.getSalaryFrom() == null ||
                        jobOffer.getSalaryTo() == null ||
                        jobOffer.getCurrency() == null ||
                        jobOffer.getContractType() == null ||
                        jobOffer.getLocation() == null ||
                        jobOffer.getFullTimeEquivalent() == null ||
                        jobOffer.getWorkModel() == null ||
                        jobOffer.getDepartment() == null ||
                        jobOffer.getRecruitmentProcessVersion() == null ||
                        jobOffer.getRecruiterId() == null ||
                        jobOffer.getSubstituteRecruiterId() == null ||
                        jobOffer.getValidFrom() == null ||
                        jobOffer.getValidTo() == null
        ) {
            throw new IllegalArgumentException("Job offer is not ready to publish");
        }
    }
}