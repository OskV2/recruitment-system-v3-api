package com.szponty.recruitment_system.joboffer.dto;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;

import java.time.LocalDateTime;
import java.util.Set;

public record PublicJobOfferDetailsResponse(
        String name,
        String description,
        int salaryFrom,
        int salaryTo,
        String currency,
        String[] mustHaveRequirements,
        String[] niceToHaveRequirements,
        LocalDateTime validFrom,
        LocalDateTime validTo,
        ContractType contractType,
        Location location,
        FullTimeEquivalent fullTimeEquivalent,
        WorkModel workModel,
        Department department,
        RecruitmentProcessVersion recruitmentProcessVersion,
        Set<JobOfferBenefit> benefits,
        int vacancy
) {
}
