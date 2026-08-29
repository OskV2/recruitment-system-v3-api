package com.szponty.recruitment_system.role.repository;

import com.szponty.recruitment_system.attachment.model.Attachment;
import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends FindOrThrowRepository<Role, UUID> {
}
