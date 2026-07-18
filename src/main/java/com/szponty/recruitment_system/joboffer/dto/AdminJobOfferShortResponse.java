package com.szponty.recruitment_system.joboffer.dto;

import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.user.model.User;

import java.time.LocalDateTime;

public record AdminJobOfferShortResponse(
        String name,
        String description,
        int salaryFrom,
        int salaryTo,
        String currency,
        LocalDateTime validFrom,
        LocalDateTime validTo,
        User recruiter,
        JobOfferStatus offerStatus,
        int vacancy
) {
}
