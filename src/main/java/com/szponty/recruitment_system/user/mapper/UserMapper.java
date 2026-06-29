package com.szponty.recruitment_system.user.mapper;

import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.role.model.Role;
import com.szponty.recruitment_system.user.dto.CreateUserRequest;
import com.szponty.recruitment_system.user.dto.UpdateUserRequest;
import com.szponty.recruitment_system.user.dto.UserResponse;
import com.szponty.recruitment_system.user.model.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getFirstName(),
                user.getEmail(),
                user.getDescription(),
                user.isLocked(),
                user.getDepartment().getId().toString(),
                user.getRole().getId().toString()
        );
    }

    public User toCreateEntity(CreateUserRequest request,
                               Role role,
                               Department department) {

        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(request.password())
                .description(request.description())
                .role(role)
                .department(department)
                .build();
    }

    public void toUpdateEntity(
            UpdateUserRequest request,
            UUID userId,
            User user,
            Role role,
            Department department
    ) {
        if (request.firstName() != null) { user.setFirstName(request.firstName()); }

        if (request.lastName() != null) {user.setLastName(request.lastName()); }

        if (request.email() != null) { user.setEmail(request.email()); }

        if (role != null) { user.setRole(role); }

        if (department != null) { user.setDepartment(department); }
    }
}
