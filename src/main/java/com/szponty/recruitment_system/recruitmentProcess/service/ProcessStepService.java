package com.szponty.recruitment_system.recruitmentProcess.service;

import com.szponty.recruitment_system.recruitmentProcess.DTO.CreateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentProcess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentProcess.DTO.UpdateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentProcess.mapper.ProcessStepMapper;
import com.szponty.recruitment_system.recruitmentProcess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentProcess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentProcess.repository.RecruitmentProcessVersionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessStepService {
    private final ProcessStepRepository processStepRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;

    private final ProcessStepMapper processStepMapper;

    public ProcessStepResponse getProcessStepById(UUID processStepId) {
        ProcessStep processStep = processStepRepository.findById(processStepId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProcessStep " + processStepId + " not found"));

        return processStepMapper.toResponse(processStep);
    }

    public ProcessStepResponse createProcessStep(CreateProcessStepRequest request) {
        RecruitmentProcessVersion recruitmentProcessVersion = recruitmentProcessVersionRepository
                .findById(request.recruitmentProcessVersion())
                .orElseThrow(() -> new IllegalArgumentException(
                        "RecruitmentProcessVersion " + request.recruitmentProcessVersion() + " not found"));

        ProcessStep step = processStepRepository.save(processStepMapper.toEntity(request, recruitmentProcessVersion));

        return processStepMapper.toResponse(step);
    }

    public List<ProcessStepResponse> getProcessStepsByRecruitmentProcessVersionId(UUID uuid) {
        return processStepRepository.findByProcessVersionIdAndDeletedFalse(uuid)
                .stream()
                .map(processStepMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProcessStepResponse updateProcessStep(UUID processStepId, UpdateProcessStepRequest request) {
        ProcessStep processStep = processStepRepository.findById(processStepId)
                .orElseThrow(() -> new IllegalArgumentException(
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
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProcessStep " + id + " not found"));

        if (processStep.isDeleted()) {
            throw new IllegalArgumentException(
                    "ProcessStep " + id + " already deleted"
            );
        }

        processStep.setDeleted(true);
    }

    public void restoreProcessStep(UUID id) {
        ProcessStep processStep = processStepRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProcessStep " + id + " not found"));

        if (!processStep.isDeleted()) {
            throw new IllegalArgumentException(
                    "ProcessStep " + id + " is not deleted"
            );
        }

        processStep.setDeleted(false);
    }
}
