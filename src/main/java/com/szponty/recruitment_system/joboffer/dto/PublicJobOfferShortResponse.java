package com.szponty.recruitment_system.joboffer.dto;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;

import java.time.LocalDateTime;
import java.util.Set;

public record PublicJobOfferShortResponse(
        String name,
        int salaryFrom,
        int salaryTo,
        String currency,
        LocalDateTime validTo,
        ContractType contractType,
        Location location,
        FullTimeEquivalent fullTimeEquivalent,
        WorkModel workModel,
        Department department,
        int vacancy
) {
}
