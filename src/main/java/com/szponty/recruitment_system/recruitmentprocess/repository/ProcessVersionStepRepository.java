package com.szponty.recruitment_system.recruitmentprocess.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessVersionStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProcessVersionStepRepository extends FindOrThrowRepository<ProcessVersionStep, UUID> {

    List<ProcessVersionStep>
    findByRecruitmentProcessVersionIdOrderByStepOrderAsc(UUID versionId);

    boolean existsByRecruitmentProcessVersionIdAndProcessStepId(
            UUID versionId,
            UUID processStepId
    );
}
