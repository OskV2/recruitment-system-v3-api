package com.szponty.recruitment_system.recruitmentprocess.repository;

import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RecruitmentProcessRepository extends JpaRepository<RecruitmentProcess, UUID> {
}
