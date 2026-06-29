package com.szponty.recruitment_system.user.controller;

import com.szponty.recruitment_system.user.dto.*;
import com.szponty.recruitment_system.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {

        UserResponse response = userService.createUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest, @PathVariable UUID id) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    @PatchMapping("/lock/{id}")
    public ResponseEntity<UserResponse> lockUser(@RequestBody LockUserRequest userRequest, @PathVariable UUID id) {
        LockUserResponse response = userService.changeUserLock(id, userRequest);

        if (!response.changed()) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }

        return ResponseEntity.ok(response.user());
    }
}
