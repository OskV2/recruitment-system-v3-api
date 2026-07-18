package com.szponty.recruitment_system.recruitmentprocess.repository;

import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecruitmentProcessVersionRepository extends JpaRepository<RecruitmentProcessVersion, UUID> {
    List<RecruitmentProcessVersion> findByRecruitmentProcess(RecruitmentProcess recruitmentProcess);
    long countByRecruitmentProcessIdAndActiveTrue(UUID recruitmentProcessId);

    @Query("""
        select distinct v from RecruitmentProcessVersion v
        join fetch v.recruitmentProcess rp
        left join fetch v.steps pvs
        left join fetch pvs.processStep ps
        where v.active = true
        and rp.deleted = false
        order by rp.name
        """)
    List<RecruitmentProcessVersion> findAllActiveWithSteps();


    @Query("""
        SELECT MAX(rpv.version)
        FROM RecruitmentProcessVersion rpv
        WHERE rpv.recruitmentProcess.id = :processId
    """)
    Optional<Integer> findMaxVersionByRecruitmentProcessId(
            UUID processId
    );
}
