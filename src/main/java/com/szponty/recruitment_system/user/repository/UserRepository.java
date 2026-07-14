package com.szponty.recruitment_system.user.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends FindOrThrowRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
