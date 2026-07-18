package com.szponty.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateProcessVersionStepRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.ProcessVersionStepResponse;
import com.szponty.recruitment_system.recruitmentprocess.mapper.ProcessVersionStepMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessVersionStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessVersionStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessVersionStepService {

    private final ProcessVersionStepRepository processVersionStepRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;
    private final ProcessStepRepository processStepRepository;

    private final ProcessVersionStepMapper processVersionStepMapper;


    @Transactional(readOnly = true)
    public List<ProcessVersionStepResponse> getStepsByVersion(UUID versionId) {

        if (!recruitmentProcessVersionRepository.existsById(versionId)) {
            throw new NotFoundException(
                    "RecruitmentProcessVersion " + versionId + " not found"
            );
        }

        return processVersionStepRepository
                .findByRecruitmentProcessVersionIdOrderByStepOrderAsc(versionId)
                .stream()
                .map(processVersionStepMapper::toResponse)
                .toList();
    }


    @Transactional
    public ProcessVersionStepResponse addStepToVersion(
            UUID versionId,
            CreateProcessVersionStepRequest request
    ) {

        RecruitmentProcessVersion version =
                recruitmentProcessVersionRepository.findById(versionId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "RecruitmentProcessVersion "
                                                + versionId
                                                + " not found"
                                )
                        );


        ProcessStep step =
                processStepRepository.findById(request.processStepId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "ProcessStep "
                                                + request.processStepId()
                                                + " not found"
                                )
                        );


        if (step.isDeleted()) {
            throw new InvalidEntityStateException(
                    "Cannot add deleted ProcessStep"
            );
        }


        ProcessVersionStep processVersionStep =
                ProcessVersionStep.builder()
                        .recruitmentProcessVersion(version)
                        .processStep(step)
                        .stepOrder(request.stepOrder())
                        .build();


        return processVersionStepMapper.toResponse(
                processVersionStepRepository.save(processVersionStep)
        );
    }

    @Transactional
    public ProcessVersionStepResponse updateStepOrder(
            UUID processVersionStepId,
            Integer newOrder
    ) {

        ProcessVersionStep step =
                processVersionStepRepository.findById(processVersionStepId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "ProcessVersionStep "
                                                + processVersionStepId
                                                + " not found"
                                )
                        );

        step.setStepOrder(newOrder);

        return processVersionStepMapper.toResponse(step);
    }

    @Transactional
    public void removeStepFromVersion(UUID processVersionStepId) {

        ProcessVersionStep step =
                processVersionStepRepository.findById(processVersionStepId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "ProcessVersionStep "
                                                + processVersionStepId
                                                + " not found"
                                )
                        );

        processVersionStepRepository.delete(step);
    }
}