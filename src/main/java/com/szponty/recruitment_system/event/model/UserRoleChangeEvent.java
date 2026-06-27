package com.szponty.recruitment_system.event.model;

import com.szponty.recruitment_system.role.model.Role;
import com.szponty.recruitment_system.user.model.User;

public record UserRoleChangeEvent(
        User createdBy,
        String firstName,
        String lastName,
        String prevRole,
        String newRole
) {
}
