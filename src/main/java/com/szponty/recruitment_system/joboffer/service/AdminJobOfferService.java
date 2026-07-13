package com.szponty.recruitment_system.joboffer.service;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.dictionary.repository.*;
import com.szponty.recruitment_system.joboffer.dto.AdminJobOfferDetailsResponse;
import com.szponty.recruitment_system.joboffer.dto.AdminJobOfferShortResponse;
import com.szponty.recruitment_system.joboffer.dto.ChangeJobOfferStatusRequest;
import com.szponty.recruitment_system.joboffer.dto.CreateJobOfferRequest;
import com.szponty.recruitment_system.joboffer.mapper.JobOfferMapper;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefitId;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.joboffer.repository.JobOfferRepository;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.user.model.User;
import com.szponty.recruitment_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminJobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;

    private final ContractTypeRepository contractTypeRepository;
    private final LocationRepository locationRepository;
    private final FullTimeEquivalentRepository fullTimeEquivalentRepository;
    private final WorkModelRepository workModelRepository;
    private final DepartmentRepository departmentRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;
    private final UserRepository userRepository;
    private final BenefitRepository benefitRepository;

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

        ContractType contractType = findContractType(request.contractTypeId());
        Location location = findLocation(request.locationId());
        FullTimeEquivalent fte = findFte(request.fullTimeEquivalentId());
        WorkModel workModel = findWorkModel(request.workModelId());
        Department department = findDepartment(request.departmentId());
        RecruitmentProcessVersion recruitmentProcessVersion =
                findRecruitmentProcessVersion(request.recruitmentProcessVersionId());
        User recruiter = findUser(request.recruiterId());
        User substituteRecruiter = findUser(request.substituteRecruiterId());

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

        Set<JobOfferBenefit> benefits = createBenefits(savedJobOffer, request.benefits());
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
                ? findContractType(request.contractTypeId())
                : null;

        Location location = request.locationId() != null
                ? findLocation(request.locationId())
                : null;

        FullTimeEquivalent fte = request.fullTimeEquivalentId() != null
                ? findFte(request.fullTimeEquivalentId())
                : null;

        WorkModel workModel = request.workModelId() != null
                ? findWorkModel(request.workModelId())
                : null;

        Department department = request.departmentId() != null
                ? findDepartment(request.departmentId())
                : null;

        RecruitmentProcessVersion recruitmentProcessVersion = request.recruitmentProcessVersionId() != null
                ? findRecruitmentProcessVersion(request.recruitmentProcessVersionId())
                : null;

        User recruiter = request.recruiterId() != null
                ? findUser(request.recruiterId())
                : null;

        User substituteRecruiter = request.substituteRecruiterId() != null
                ? findUser(request.substituteRecruiterId())
                : null;

        LocalDateTime validFrom = request.validFrom() != null
                ? request.validFrom()
                : null;

        LocalDateTime validTo = request.validTo() != null
                ? request.validTo()
                : null;

        JobOfferStatus status = request.offerStatus() != null
                ? request.offerStatus()
                : null;

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
            updateBenefits(jobOffer, request.benefits());
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
        return jobOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job offer not found"));
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

    private ContractType findContractType(String id) {
        return contractTypeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Contract type not found"));
    }

    private Location findLocation(String id) {
        return locationRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
    }

    private FullTimeEquivalent findFte(String id) {
        return fullTimeEquivalentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Full time equivalent not found"));
    }

    private WorkModel findWorkModel(String id) {
        return workModelRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Work model not found"));
    }

    private Department findDepartment(String id) {
        return departmentRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));
    }

    private RecruitmentProcessVersion findRecruitmentProcessVersion(String id) {
        return recruitmentProcessVersionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Recruitment process version not found"));
    }

    private User findUser(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Set<JobOfferBenefit> createBenefits(JobOffer jobOffer, Set<UUID> benefitIds) {
        if (benefitIds == null || benefitIds.isEmpty()) {
            return Set.of();
        }

        return benefitIds.stream()
                .map(benefitId -> {
                    Benefit benefit = benefitRepository.findById(benefitId)
                            .orElseThrow(() -> new IllegalArgumentException("Benefit not found"));

                    return JobOfferBenefit.builder()
                            .jobOffer(jobOffer)
                            .benefit(benefit)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    private void updateBenefits(JobOffer jobOffer, Set<UUID> requestedBenefitIds) {
        jobOffer.getBenefits().removeIf(jobOfferBenefit ->
                !requestedBenefitIds.contains(jobOfferBenefit.getBenefit().getId())
        );

        Set<UUID> currentBenefitIds = jobOffer.getBenefits()
                .stream()
                .map(jobOfferBenefit -> jobOfferBenefit.getBenefit().getId())
                .collect(Collectors.toSet());

        Set<UUID> benefitIdsToAdd = requestedBenefitIds
                .stream()
                .filter(benefitId -> !currentBenefitIds.contains(benefitId))
                .collect(Collectors.toSet());

        List<Benefit> benefitsToAdd = benefitRepository.findAllById(benefitIdsToAdd);

        if (benefitsToAdd.size() != benefitIdsToAdd.size()) {
            throw new IllegalArgumentException("One or more benefits were not found");
        }

        benefitsToAdd.forEach(benefit -> {
            JobOfferBenefit jobOfferBenefit = JobOfferBenefit.builder()
                    .id(new JobOfferBenefitId(jobOffer.getId(), benefit.getId()))
                    .jobOffer(jobOffer)
                    .benefit(benefit)
                    .build();

            jobOffer.getBenefits().add(jobOfferBenefit);
        });
    }
}
