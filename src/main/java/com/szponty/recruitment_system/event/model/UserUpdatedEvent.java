package com.szponty.recruitment_system.event.model;

import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.role.model.Role;
import com.szponty.recruitment_system.user.model.User;

public record UserUpdatedEvent(
        User createdBy,
        Role role,
        Department department
) {
}
