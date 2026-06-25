package com.szponty.recruitment_system.user.dto;

import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.role.model.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String description,
        boolean locked,
        String departmentId,
        String roleId
) {
}
