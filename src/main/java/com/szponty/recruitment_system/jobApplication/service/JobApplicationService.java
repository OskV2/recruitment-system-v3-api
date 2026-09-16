package com.szponty.recruitment_system.jobApplication.service;


import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.jobApplication.DTO.CreateJobApplicationRequest;
import com.szponty.recruitment_system.jobApplication.DTO.JobApplicationResponse;
import com.szponty.recruitment_system.jobApplication.mapper.JobApplicationMapper;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStatus;
import com.szponty.recruitment_system.jobApplication.repository.JobApplicationRepository;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.joboffer.repository.JobOfferRepository;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobOfferRepository jobOfferRepository;

    private final JobApplicationMapper jobApplicationMapper;

    @Transactional(readOnly = true)
    public JobApplicationResponse getJobApplicationById(UUID id) {
        JobApplication jobApplication = jobApplicationRepository.getOrThrow(id, "Job Application");
        return jobApplicationMapper.toResponse(jobApplication);
    }

    @Transactional(readOnly = true)
    public List<JobApplicationResponse> getAllJobApplications(
            Optional<UUID> jobOfferId,
            Optional<JobApplicationStatus> jobApplicationStatus
    ) {
        List<JobApplication> jobApplications = jobApplicationRepository.findAllByOptionalFilters(
                jobOfferId.orElse(null),
                jobApplicationStatus.orElse(null)
        );

        return jobApplications.stream()
                .map(jobApplicationMapper::toResponse)
                .toList();
    }

    @Transactional
    public JobApplicationResponse createJobApplication(CreateJobApplicationRequest request) {
        JobOffer jobOffer = jobOfferRepository.getOrThrow(request.jobOfferId(), "Job Offer");

        LocalDateTime now = LocalDateTime.now();

        boolean isOpenForApplications =
                jobOffer.getOfferStatus() == JobOfferStatus.ACTIVE
                        && jobOffer.getValidFrom() != null
                        && jobOffer.getValidTo() != null
                        && !jobOffer.getValidFrom().isAfter(now)
                        && !jobOffer.getValidTo().isBefore(now);

        if (!isOpenForApplications) {
            throw new InvalidEntityStateException("Job offer " + jobOffer.getId() + " is not open for applications");
        }

        JobApplication jobApplication = JobApplication.builder()
                .jobOffer(jobOffer)
                .publicToken(UUID.randomUUID())
                .recruitmentProcessVersion(jobOffer.getRecruitmentProcessVersion())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .status(JobApplicationStatus.SUBMITTED)
                .githubLink(request.githubLink())
                .build();

        JobApplication saved = jobApplicationRepository.save(jobApplication);
        return jobApplicationMapper.toResponse(saved);
    }

    @Transactional
    public void restoreJobApplication(UUID id) {
        JobApplication jobApplication = jobApplicationRepository.getOrThrow(id, "Job Application");

        if (!jobApplication.isDeleted()) {
            throw new InvalidEntityStateException(
                    "JobApplication " + id + " already active"
            );
        }

        jobApplication.setDeleted(false);
    }

    @Transactional
    public void deleteJobApplication(UUID id) {
        JobApplication jobApplication = jobApplicationRepository.getOrThrow(id, "Job Application");

        if (jobApplication.isDeleted()) {
            throw new InvalidEntityStateException(
                    "JobApplication " + id + " already deleted"
            );
        }

        jobApplication.setDeleted(true);
    }

}
