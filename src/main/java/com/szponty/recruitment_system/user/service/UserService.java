package com.szponty.recruitment_system.user.service;


import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.dictionary.repository.DepartmentRepository;
import com.szponty.recruitment_system.event.model.UserCreatedEvent;
import com.szponty.recruitment_system.role.model.Role;
import com.szponty.recruitment_system.role.repository.RoleRepository;
import com.szponty.recruitment_system.user.dto.CreateUserRequest;
import com.szponty.recruitment_system.user.dto.UserResponse;
import com.szponty.recruitment_system.user.mapper.UserMapper;
import com.szponty.recruitment_system.user.model.User;
import com.szponty.recruitment_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse createUser(CreateUserRequest request) {

        userRepository.findByEmail(request.email())
                .ifPresent(_ -> {
                    throw new IllegalArgumentException("User with email " + request.email() + " already exists");
                });

        UUID roleId = parseUuid(request.roleId(), "role id");
        UUID departmentId = parseUuid(request.departmentId(), "department id");

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Role with id " + roleId + " not found"));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Department with id " + departmentId + " not found"));

        User user = userRepository.save(userMapper.toEntity(request, role, department));

        eventPublisher.publishEvent(new UserCreatedEvent(
            user.getId(), user.getFirstName() + " " + user.getLastName()
        ));

        return userMapper.toResponse(user);
    }


    private UUID parseUuid(String id, String fieldName) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + fieldName);
        }
    }
}
