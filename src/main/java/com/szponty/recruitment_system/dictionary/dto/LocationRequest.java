package com.szponty.recruitment_system.dictionary.dto;

public record LocationRequest(
    String city,
    String country,
    String description
) {
}
