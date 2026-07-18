package com.szponty.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.LastActiveVersionException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessVersionRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentprocess.mapper.RecruitmentProcessVersionMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessVersionService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessVersionRepository versionRepository;

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
    public RecruitmentProcessVersionResponse createRecruitmentProcessVersion(
        UUID recruitmentProcessId
    ) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcess with id " + recruitmentProcessId + " was not found."
                ));

        RecruitmentProcessVersion version = RecruitmentProcessVersion.builder()
                .recruitmentProcess(recruitmentProcess)
                .version(getNextVersionNumber(recruitmentProcessId))
                .build();

        RecruitmentProcessVersion savedVersion =
                versionRepository.save(version);

        return recruitmentProcessVersionMapper.toResponse(savedVersion);
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
    public void activateRecruitmentProcessVersion(UUID id) {
        RecruitmentProcessVersion recruitmentProcessVersion = versionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcessVersion " + id + " not found"
                ));

        if (recruitmentProcessVersion.isActive()) {
            throw new InvalidEntityStateException(
                    "RecruitmentProcessVersion " + id + " is already active"
            );
        }

        recruitmentProcessVersion.setActive(true);
    }

    private Integer getNextVersionNumber(UUID recruitmentProcessId) {

        return versionRepository
                .findMaxVersionByRecruitmentProcessId(recruitmentProcessId)
                .orElse(0) + 1;
    }
}
