package com.szponty.recruitment_system.event.model;

import com.szponty.recruitment_system.user.model.User;

public record UserLockChangeEvent(
        User createdBy,
        String firstName,
        String lastName,
        boolean locked
) {
}
