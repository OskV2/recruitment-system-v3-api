package com.szponty.recruitment_system.joboffer.dto;

import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record CreateJobOfferRequest(
        String name,
        String description,
        Integer salaryFrom,
        Integer salaryTo,
        String currency,
        String[] mustHaveRequirements,
        String[] niceToHaveRequirements,
        LocalDateTime validFrom,
        LocalDateTime validTo,
        String contractTypeId,
        String locationId,
        String fullTimeEquivalentId,
        String workModelId,
        String departmentId,
        String recruitmentProcessVersionId,
        String recruiterId,
        String substituteRecruiterId,
        Set<UUID> benefits,
        JobOfferStatus offerStatus,
        Integer vacancy
) {
}
