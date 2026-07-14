package com.szponty.recruitment_system.joboffer.dto;

import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;

public record ChangeJobOfferStatusRequest(
        JobOfferStatus offerStatus
) {
}
