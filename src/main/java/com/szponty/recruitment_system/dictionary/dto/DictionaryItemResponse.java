package com.szponty.recruitment_system.dictionary.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DictionaryItemResponse(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt
) {
}
