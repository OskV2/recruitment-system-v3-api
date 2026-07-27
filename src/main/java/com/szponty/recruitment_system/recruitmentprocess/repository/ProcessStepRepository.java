package com.szponty.recruitment_system.recruitmentprocess.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcessStepRepository extends FindOrThrowRepository<ProcessStep, UUID> {
    List<ProcessStep> findByDeleted(boolean deleted);
}
