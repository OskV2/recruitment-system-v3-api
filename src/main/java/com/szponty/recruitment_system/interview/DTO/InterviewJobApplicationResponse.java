package com.szponty.recruitment_system.interview.DTO;

import java.util.UUID;

public record InterviewJobApplicationResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String jobOfferName
) {
}
