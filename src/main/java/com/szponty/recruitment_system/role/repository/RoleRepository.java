package com.szponty.recruitment_system.role.repository;

import com.szponty.recruitment_system.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
