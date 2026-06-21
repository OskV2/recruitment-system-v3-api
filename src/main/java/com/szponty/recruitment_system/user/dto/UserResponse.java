package com.szponty.recruitment_system.user.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String description
) {
}
