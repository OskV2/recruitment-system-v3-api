package com.szponty.recruitment_system.joboffer.resolver;

import com.szponty.recruitment_system.dictionary.model.*;
import com.szponty.recruitment_system.dictionary.repository.*;

import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentProcess.repository.RecruitmentProcessVersionRepository;

import com.szponty.recruitment_system.user.model.User;
import com.szponty.recruitment_system.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobOfferReferenceResolver {

    private final ContractTypeRepository contractTypeRepository;
    private final LocationRepository locationRepository;
    private final FullTimeEquivalentRepository fullTimeEquivalentRepository;
    private final WorkModelRepository workModelRepository;
    private final DepartmentRepository departmentRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;
    private final UserRepository userRepository;

    public ContractType resolveContractType(String id) {
        return contractTypeRepository.getOrThrow(UUID.fromString(id), "Contract type");
    }

    public Location resolveLocation(String id) {
        return locationRepository.getOrThrow(UUID.fromString(id), "Location");
    }

    public FullTimeEquivalent resolveFullTimeEquivalent(String id) {
        return fullTimeEquivalentRepository.getOrThrow(UUID.fromString(id), "Full time equivalent");
    }

    public WorkModel resolveWorkModel(String id) {
        return workModelRepository.getOrThrow(UUID.fromString(id), "Work model");
    }

    public Department resolveDepartment(String id) {
        return departmentRepository.getOrThrow(UUID.fromString(id), "Department");
    }

    public RecruitmentProcessVersion resolveRecruitmentProcessVersion(String id) {
        return recruitmentProcessVersionRepository.getOrThrow(UUID.fromString(id), "Recruitment process version");
    }

    public User resolveUser(String id) {
        return userRepository.getOrThrow(UUID.fromString(id), "User");
    }
}