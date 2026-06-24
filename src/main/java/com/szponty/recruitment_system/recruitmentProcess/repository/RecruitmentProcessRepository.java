package com.szponty.recruitment_system.recruitmentProcess.repository;

import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, UUID> {
}
