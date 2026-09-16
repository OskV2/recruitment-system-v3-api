package com.szponty.recruitment_system.jobApplication.DTO;

import java.util.UUID;

public record CreateJobApplicationRequest(
        UUID jobOfferId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String githubLink
) {
}
