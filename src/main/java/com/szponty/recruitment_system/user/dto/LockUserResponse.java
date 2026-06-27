package com.szponty.recruitment_system.user.dto;

public record LockUserResponse(
        UserResponse user,
        boolean changed
) {
}
