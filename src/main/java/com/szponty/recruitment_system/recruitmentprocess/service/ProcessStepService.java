package com.szponty.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidProcessStepStateException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentprocess.DTO.UpdateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentprocess.mapper.ProcessStepMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessStepService {
    private final ProcessStepRepository processStepRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;

    private final ProcessStepMapper processStepMapper;

    @Transactional(readOnly = true)
    public List<ProcessStepResponse> getAllSteps(boolean deleted) {
        return processStepRepository.findByDeleted(deleted)
                .stream()
                .map(processStepMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProcessStepResponse getProcessStepById(UUID processStepId) {
        ProcessStep processStep = processStepRepository.findById(processStepId)
                .orElseThrow(() -> new NotFoundException(
                        "ProcessStep " + processStepId + " not found"));

        return processStepMapper.toResponse(processStep);
    }

    @Transactional
    public ProcessStepResponse createProcessStep(@NonNull CreateProcessStepRequest request) {
        RecruitmentProcessVersion recruitmentProcessVersion = recruitmentProcessVersionRepository
                .findById(request.recruitmentProcessVersion())
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcessVersion " + request.recruitmentProcessVersion() + " not found"));

        ProcessStep step = processStepRepository.save(processStepMapper.toEntity(request, recruitmentProcessVersion));

        return processStepMapper.toResponse(step);
    }

    @Transactional(readOnly = true)
    public List<ProcessStepResponse> getProcessStepsByRecruitmentProcessVersionId(UUID uuid) {
        return processStepRepository.findByProcessVersionIdAndDeletedFalse(uuid)
                .stream()
                .map(processStepMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProcessStepResponse updateProcessStep(UUID processStepId, @NonNull UpdateProcessStepRequest request) {
        ProcessStep processStep = processStepRepository.findById(processStepId)
                .orElseThrow(() -> new NotFoundException(
                        "ProcessStep " + processStepId + " not found"));


        if (request.name() != null) {
            processStep.setName(request.name());
        }

        if (request.description() != null) {
            processStep.setDescription(request.description());
        }

        if (request.requiresInterview() != null) {
            processStep.setRequiresInterview(request.requiresInterview());
        }

        if (request.requiresDepartmentApproval() != null) {
            processStep.setRequiresDepartmentApproval(
                    request.requiresDepartmentApproval()
            );
        }

        return processStepMapper.toResponse(processStep);
    }

    @Transactional
    public void deleteProcessStep(UUID id) {
        ProcessStep processStep = processStepRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "ProcessStep " + id + " not found"));

        if (processStep.isDeleted()) {
            throw new InvalidProcessStepStateException(
                    "ProcessStep " + id + " already deleted"
            );
        }

        processStep.setDeleted(true);
    }

    @Transactional
    public void restoreProcessStep(UUID id) {
        ProcessStep processStep = processStepRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "ProcessStep " + id + " not found"));

        if (!processStep.isDeleted()) {
            throw new InvalidProcessStepStateException(
                    "ProcessStep " + id + " is not deleted"
            );
        }

        processStep.setDeleted(false);
    }
}
