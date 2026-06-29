package com.szponty.recruitment_system.recruitmentProcess.repository;

import com.szponty.recruitment_system.recruitmentProcess.model.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcessStepRepository extends JpaRepository<ProcessStep, UUID> {
    List<ProcessStep> findByProcessVersion_Id(UUID processVersionId);
    List<ProcessStep> findByProcessVersionIdAndDeletedFalse(UUID processVersionId);

    List<ProcessStep> findByDeleted(boolean deleted);
}
