package com.szponty.recruitment_system.event.model;

import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.user.model.User;

public record UserDepartmentChangedEvent(
        User createdBy,
        String firstName,
        String lastName,
        String prevDepartment,
        String newDepartment
) {
}
