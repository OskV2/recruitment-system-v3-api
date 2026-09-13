package com.szponty.recruitment_system.jobApplication.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStep;

import java.util.UUID;

public interface JobApplicationStepRepository extends FindOrThrowRepository<JobApplicationStep, UUID> {
}
