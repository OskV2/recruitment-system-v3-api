package com.szponty.recruitment_system.recruitmentprocess.repository;

import com.szponty.recruitment_system.recruitmentprocess.model.ProcessVersionStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProcessVersionStepRepository extends JpaRepository<ProcessVersionStep, UUID> {

    List<ProcessVersionStep>
    findByRecruitmentProcessVersionIdOrderByStepOrderAsc(UUID versionId);

    boolean existsByRecruitmentProcessVersionIdAndProcessStepId(
            UUID versionId,
            UUID processStepId
    );
}
