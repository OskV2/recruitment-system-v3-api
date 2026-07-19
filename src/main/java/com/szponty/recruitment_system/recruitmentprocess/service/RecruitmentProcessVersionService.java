package com.szponty.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.LastActiveVersionException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessVersionRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentprocess.mapper.RecruitmentProcessVersionMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessVersionStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessVersionService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessVersionRepository versionRepository;
    private final ProcessStepRepository processStepRepository;

    private final RecruitmentProcessVersionMapper recruitmentProcessVersionMapper;

    @Transactional(readOnly = true)
    public List<RecruitmentProcessVersion> getAllVersions(UUID processId) {
        return versionRepository.findAllByRecruitmentProcessIdOrderByVersionDesc(processId);
    }

    @Transactional(readOnly = true)
    public RecruitmentProcessVersion getActiveVersion(UUID processId) {
        return versionRepository.findByRecruitmentProcessIdAndActiveTrue(processId)
                .orElseThrow(() -> new NotFoundException(
                        "No active version found for RecruitmentProcess " + processId
                ));
    }

    @Transactional(readOnly = true)
    public RecruitmentProcessVersion getVersionById(UUID processId, UUID versionId) {
        RecruitmentProcessVersion v = versionRepository.findById(versionId)
                .orElseThrow(() -> new NotFoundException("Version " + versionId + " not found"));

        if (!v.getRecruitmentProcess().getId().equals(processId)) {
            throw new NotFoundException(
                    "Version " + versionId + " does not belong to RecruitmentProcess " + processId
            );
        }
        return v;
    }

    @Transactional(readOnly = true)
    public RecruitmentProcessVersion getVersionByNumber(UUID processId, int versionNumber) {
        return versionRepository.findByRecruitmentProcessIdAndVersion(processId, versionNumber)
                .orElseThrow(() -> new NotFoundException(
                        "Version " + versionNumber + " not found for RecruitmentProcess " + processId
                ));
    }

    @Transactional
    public RecruitmentProcessVersion createNewVersion(RecruitmentProcess process, List<UUID> processStepIds) {
        List<ProcessStep> steps = resolveSteps(processStepIds);

        versionRepository.findByRecruitmentProcessIdAndActiveTrue(process.getId())
                .ifPresent(current -> current.setActive(false));

        int nextVersionNumber = versionRepository
                .findTopByRecruitmentProcessIdOrderByVersionDesc(process.getId())
                .map(v -> v.getVersion() + 1)
                .orElse(1);

        RecruitmentProcessVersion newVersion = RecruitmentProcessVersion.builder()
                .recruitmentProcess(process)
                .version(nextVersionNumber)
                .active(true)
                .build();

        List<ProcessVersionStep> versionSteps = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            versionSteps.add(ProcessVersionStep.builder()
                    .recruitmentProcessVersion(newVersion)
                    .processStep(steps.get(i))
                    .stepOrder(i + 1)
                    .build());
        }
        newVersion.setSteps(versionSteps);

        return versionRepository.save(newVersion);
    }

    private List<ProcessStep> resolveSteps(List<UUID> processStepIds) {
        if (processStepIds == null || processStepIds.isEmpty()) {
            throw new InvalidEntityStateException("At least one process step is required");
        }
        if (processStepIds.size() != Set.copyOf(processStepIds).size()) {
            throw new InvalidEntityStateException("Duplicate process step ids are not allowed");
        }

        List<ProcessStep> steps = processStepRepository.findAllById(processStepIds);
        if (steps.size() != processStepIds.size()) {
            Set<UUID> found = steps.stream().map(ProcessStep::getId).collect(Collectors.toSet());
            List<UUID> missing = processStepIds.stream().filter(pid -> !found.contains(pid)).toList();
            throw new NotFoundException("Process steps not found: " + missing);
        }

        Map<UUID, ProcessStep> byId = steps.stream().collect(Collectors.toMap(ProcessStep::getId, s -> s));
        return processStepIds.stream().map(byId::get).toList();
    }


    @Transactional
    public void inactivateRecruitmentProcessVersion(UUID id) {
        RecruitmentProcessVersion recruitmentProcessVersion = versionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcessVersion " + id + " not found"
                ));

        long activeVersions =
                versionRepository.countByRecruitmentProcessIdAndActiveTrue(
                        recruitmentProcessVersion.getRecruitmentProcess().getId()
                );

        if (activeVersions <= 1) {
            throw new LastActiveVersionException(
                    "Recruitment process must have at least one active version."
            );
        }

        if (!recruitmentProcessVersion.isActive()) {
            throw new InvalidEntityStateException(
                    "RecruitmentProcessVersion " + id + " already not active"
            );
        }

        recruitmentProcessVersion.setActive(false);
    }

    @Transactional
    public void activateVersion(UUID processId, UUID versionId) {
        RecruitmentProcessVersion versionToActivate = versionRepository.findById(versionId)
                .orElseThrow(() -> new NotFoundException("Version " + versionId + " not found"));

        if (!versionToActivate.getRecruitmentProcess().getId().equals(processId)) {
            throw new NotFoundException(
                    "Version " + versionId + " does not belong to RecruitmentProcess " + processId
            );
        }

        if (versionToActivate.isActive()) {
            throw new InvalidEntityStateException("Version " + versionId + " is already active");
        }

        versionRepository.findByRecruitmentProcessIdAndActiveTrue(processId)
                .ifPresent(currentActive -> currentActive.setActive(false));

        versionToActivate.setActive(true);
    }

    private Integer getNextVersionNumber(UUID recruitmentProcessId) {

        return versionRepository
                .findMaxVersionByRecruitmentProcessId(recruitmentProcessId)
                .orElse(0) + 1;
    }
}
