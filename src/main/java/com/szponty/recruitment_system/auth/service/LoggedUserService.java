package com.szponty.recruitment_system.auth.service;


import com.szponty.recruitment_system.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service

public class LoggedUserService {
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new IllegalStateException("User is not authenticated");
        }

        return user;
    }
}
