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
    private final ProcessStepRepository processStepRepository;
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;

    private final RecruitmentProcessVersionMapper recruitmentProcessVersionMapper;

    @Transactional(readOnly = true)
    public RecruitmentProcessVersionResponse getRecruitmentProcessVersionById(UUID id) {
        RecruitmentProcessVersion recruitmentProcessVersion = recruitmentProcessVersionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcessVersion with id" + id + " was not found."
                ));

        return recruitmentProcessVersionMapper.toResponse(recruitmentProcessVersion);
    }

    @Transactional(readOnly = true)
    public List<RecruitmentProcessVersionResponse> getAllRecruitmentProcVersionsByRecruitmentProcessId(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcess with id " + id + " was not found."
                ));

        return recruitmentProcessVersionRepository.findByRecruitmentProcess(recruitmentProcess)
                        .stream()
                        .map(recruitmentProcessVersionMapper::toResponse)
                        .toList();
    }

    @Transactional
    public RecruitmentProcessVersionResponse createRecruitmentProcessVersion(
        UUID recruitmentProcessId,
        CreateRecruitmentProcessVersionRequest request
    ) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(recruitmentProcessId)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcess with id " + recruitmentProcessId + " was not found."
                ));

        RecruitmentProcessVersion version = RecruitmentProcessVersion.builder()
                .recruitmentProcess(recruitmentProcess)
                .version(UUID.randomUUID())
                .build();

        List<UUID> stepsToFind = request.stepIds();

        List<ProcessStep> stepList = processStepRepository.findAllById(stepsToFind);

        version.setSteps(stepList);

        RecruitmentProcessVersion savedVersion =
                recruitmentProcessVersionRepository.save(version);

        return recruitmentProcessVersionMapper.toResponse(savedVersion);
    }


    @Transactional
    public void inactivateRecruitmentProcessVersion(UUID id) {
        RecruitmentProcessVersion recruitmentProcessVersion = recruitmentProcessVersionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcessVersion " + id + " not found"
                ));

        long activeVersions =
                recruitmentProcessVersionRepository.countByRecruitmentProcessIdAndActiveTrue(
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
        RecruitmentProcessVersion recruitmentProcessVersion = recruitmentProcessVersionRepository.findById(id)
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
}
