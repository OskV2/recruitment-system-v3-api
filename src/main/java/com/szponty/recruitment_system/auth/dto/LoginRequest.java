package com.szponty.recruitment_system.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}
