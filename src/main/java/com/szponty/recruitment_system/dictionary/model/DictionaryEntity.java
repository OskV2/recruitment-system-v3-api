package com.szponty.recruitment_system.dictionary.model;

import java.time.LocalDateTime;
import java.util.UUID;

public interface DictionaryEntity {
    UUID getId();

    String getName();
    void setName(String name);

    String getDescription();
    void setDescription(String description);

    boolean isDeleted();
    void setDeleted(boolean deleted);

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}
