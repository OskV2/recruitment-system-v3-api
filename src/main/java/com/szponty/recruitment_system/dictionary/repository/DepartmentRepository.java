package com.szponty.recruitment_system.dictionary.repository;

import com.szponty.recruitment_system.dictionary.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
}
