package com.szponty.recruitment_system.user.mapper;

import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.role.model.Role;
import com.szponty.recruitment_system.user.dto.CreateUserRequest;
import com.szponty.recruitment_system.user.dto.UserResponse;
import com.szponty.recruitment_system.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getFirstName(),
                user.getEmail(),
                user.getDescription()
        );
    }

    public User toEntity(CreateUserRequest request,
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
}
