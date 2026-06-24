package com.szponty.recruitment_system.recruitmentProcess.repository;

import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecruitmentProcessVersionRepository extends JpaRepository<RecruitmentProcessVersion, UUID> {
}
