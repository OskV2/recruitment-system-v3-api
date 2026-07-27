package com.szponty.recruitment_system.recruitmentprocess.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecruitmentProcessVersionRepository extends FindOrThrowRepository<RecruitmentProcessVersion, UUID> {

    @Query("""
    SELECT DISTINCT v FROM RecruitmentProcessVersion v
    JOIN FETCH v.recruitmentProcess rp
    LEFT JOIN FETCH v.steps pvs
    LEFT JOIN FETCH pvs.processStep ps
    WHERE rp.deleted = false
    ORDER BY rp.name, v.version
    """)
    List<RecruitmentProcessVersion> findAllWithSteps();

    List<RecruitmentProcessVersion> findAllByRecruitmentProcessIdOrderByVersionDesc(UUID processId);

    Optional<RecruitmentProcessVersion> findByRecruitmentProcessIdAndActiveTrue(UUID processId);

    Optional<RecruitmentProcessVersion> findByRecruitmentProcessIdAndVersion(UUID processId, int version);

    Optional<RecruitmentProcessVersion> findTopByRecruitmentProcessIdOrderByVersionDesc(UUID processId);
}
