package com.szponty.recruitment_system.jobApplication.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JobApplicationRepository extends FindOrThrowRepository<JobApplication, UUID> {
    @Query("""
        SELECT ja FROM JobApplication ja
        WHERE ja.deleted = false
        AND (:jobOfferId IS NULL OR ja.jobOffer.id = :jobOfferId)
        AND (:status IS NULL OR ja.status = :status)   
    """)
    List<JobApplication> findAllByOptionalFilters(
            @Param("jobOfferId") UUID jobOfferId,
            @Param("status") JobApplicationStatus status
    );
}
