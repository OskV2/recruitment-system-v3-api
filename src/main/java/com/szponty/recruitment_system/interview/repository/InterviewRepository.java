package com.szponty.recruitment_system.interview.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.interview.model.Interview;
import com.szponty.recruitment_system.interview.model.InterviewStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterviewRepository extends FindOrThrowRepository<Interview, UUID> {


    @Query("""
        SELECT i FROM Interview i
        WHERE i.recruiter.id = :recruiterId
        AND i.deleted = false
        AND (:status IS NULL OR i.status = :status)
    """)
    List<Interview> findByRecruiterIdAndOptionalStatus(
            @Param("recruiterId") UUID recruiterId,
            @Param("status") InterviewStatus status
    );

    @Query("""
        SELECT i
        FROM Interview i
        WHERE i.id = :interviewId
          AND i.jobApplicationStep.jobApplication.id = :jobApplicationId
    """)
    Optional<Interview> findByIdAndJobApplicationId(UUID interviewId, UUID jobApplicationId);

    @Query("""
        SELECT i
        FROM Interview i
        WHERE i.jobApplicationStep.jobApplication.id = :jobApplicationId
    """)
    List<Interview> findByJobApplicationId(UUID jobApplicationId);
}
