package com.szponty.recruitment_system.dictionary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record LocationResponse(
    UUID id,
    String city,
    String country,
    String description,
    LocalDateTime createdAt
) {
}
