package com.szponty.recruitment_system.recruitmentProcess.repository;

import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecruitmentProcessVersionRepository extends JpaRepository<RecruitmentProcessVersion, UUID> {
    List<RecruitmentProcessVersion> findByRecruitmentProcess(RecruitmentProcess recruitmentProcess);
}
