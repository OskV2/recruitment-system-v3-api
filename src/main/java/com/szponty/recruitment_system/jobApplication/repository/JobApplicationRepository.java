package com.szponty.recruitment_system.jobApplication.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;

import java.util.UUID;

public interface JobApplicationRepository extends FindOrThrowRepository<JobApplication, UUID> {
}
