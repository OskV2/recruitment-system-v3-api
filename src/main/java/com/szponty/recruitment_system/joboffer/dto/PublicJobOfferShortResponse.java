package com.szponty.recruitment_system.joboffer.dto;

import java.time.LocalDateTime;

public record PublicJobOfferShortResponse(
        String name,
        int salaryFrom,
        int salaryTo,
        String currency,
        LocalDateTime validTo,
        String contractType,
        String location,
        String fullTimeEquivalent,
        String workModel,
        String department,
        int vacancy
) {

}
