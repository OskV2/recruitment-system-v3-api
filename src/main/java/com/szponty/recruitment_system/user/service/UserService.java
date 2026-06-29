package com.szponty.recruitment_system.user.service;

import com.szponty.recruitment_system.auth.service.LoggedUserService;
import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.dictionary.repository.DepartmentRepository;
import com.szponty.recruitment_system.event.model.*;
import com.szponty.recruitment_system.role.model.Role;
import com.szponty.recruitment_system.role.repository.RoleRepository;
import com.szponty.recruitment_system.user.dto.*;
import com.szponty.recruitment_system.user.mapper.UserMapper;
import com.szponty.recruitment_system.user.model.User;
import com.szponty.recruitment_system.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final LoggedUserService loggedUserService;

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

        User user = userRepository.save(userMapper.toCreateEntity(request, role, department));
        User createdBy = loggedUserService.getCurrentUser();

        eventPublisher.publishEvent(new UserCreatedEvent(
            createdBy, user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole(), user.getDepartment()
        ));

        return userMapper.toResponse(user);
    }

    public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        Department userDepartment = user.getDepartment();
        String userDepartmentId = userDepartment.getId().toString();
        String userDepartmentName = userDepartment.getName();

        Role userRole = user.getRole();
        String userRoleId = userRole.getId().toString();
        String userRoleName = userRole.getName();

        UUID roleId = parseUuid(request.roleId(), "role id");
        UUID departmentId = parseUuid(request.departmentId(), "department id");

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Role with id " + roleId + " not found"));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Department with id " + departmentId + " not found"));

        userMapper.toUpdateEntity(request, userId, user, role, department);

        User savedUser = userRepository.save(user);
        User createdBy = loggedUserService.getCurrentUser();

        if (!userDepartmentId.equals(request.departmentId())) {
            eventPublisher.publishEvent(new UserDepartmentChangedEvent(
                    createdBy, savedUser.getFirstName(), savedUser.getLastName(), userDepartmentName, savedUser.getDepartment().getName()
            ));
        }

        if (!userRoleId.equals(request.roleId())) {
            eventPublisher.publishEvent(new UserRoleChangeEvent(
                    createdBy, savedUser.getFirstName(), savedUser.getLastName(), userRoleName, savedUser.getRole().getName()
            ));
        }

        return userMapper.toResponse(savedUser);
    }

    public LockUserResponse changeUserLock(UUID userId, LockUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        if (user.isLocked() == request.locked()) {
            return new LockUserResponse(userMapper.toResponse(user), false);
        }

        user.setLocked(request.locked());

        User savedUser = userRepository.save(user);
        User createdBy = loggedUserService.getCurrentUser();

        eventPublisher.publishEvent(new UserLockChangeEvent(
                createdBy, savedUser.getFirstName(), savedUser.getLastName(), request.locked()
        ));

        return new LockUserResponse(userMapper.toResponse(user), true);
    }

    private UUID parseUuid(String id, String fieldName) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid " + fieldName);
        }
    }
}
