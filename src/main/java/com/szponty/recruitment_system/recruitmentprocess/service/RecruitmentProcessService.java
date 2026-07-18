package com.szponty.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentprocess.mapper.RecruitmentProcessMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;
    private final RecruitmentProcessMapper processMapper;

    private final RecruitmentProcessVersionService recruitmentProcessVersionService;

    @Transactional(readOnly = true)
    public List<RecruitmentProcessResponse> getAllRecruitmentProcesses() {
        return recruitmentProcessVersionRepository.findAllActiveWithSteps()
                .stream()
                .map(processMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecruitmentProcessResponse> getRecruitmentProcessById(UUID id, String version, boolean includeSteps) {
        RecruitmentProcess process = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("RecruitmentProcess " + id + " not found"));

        List<RecruitmentProcessVersion> versions = resolveVersions(id, version);

        return versions.stream()
                .map(v -> includeSteps ? processMapper.toResponse(v) : processMapper.toResponseWithoutSteps(v))
                .toList();
    }

    private List<RecruitmentProcessVersion> resolveVersions(UUID processId, String version) {
        if ("all".equalsIgnoreCase(version)) {
            return recruitmentProcessVersionService.getAllVersions(processId);
        }
        if ("latest".equalsIgnoreCase(version)) {
            return List.of(recruitmentProcessVersionService.getActiveVersion(processId));
        }
        int versionNumber = parseVersionNumber(version);
        return List.of(recruitmentProcessVersionService.getVersionByNumber(processId, versionNumber));
    }

    private int parseVersionNumber(String version) {
        try {
            return Integer.parseInt(version);
        } catch (NumberFormatException e) {
            throw new InvalidEntityStateException(
                    "Invalid version parameter: '" + version + "'. Expected 'all', 'latest' or a version number."
            );
        }
    }


//    @Transactional(readOnly = true)
//    public RecruitmentProcessResponse getRecruitmentProcessById(UUID id) {
//        return new RecruitmentProcessResponse(
//    }

//    @Transactional
//    public RecruitmentProcessResponse createRecruitmentProcess(CreateRecruitmentProcessRequest request) {
//        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(processMapper.toEntity(request));
//        return processMapper.toResponse(recruitmentProcess);
//    }

    @Transactional
    public void deleteRecruitmentProcess(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcess " + id + " not found"
                ));

        if (recruitmentProcess.isDeleted()) {
            throw new InvalidEntityStateException(
                    "RecruitmentProcess " + id + " already deleted"
            );
        }

        recruitmentProcess.setDeleted(true);
    }

    @Transactional
    public void restoreRecruitmentProcess(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "RecruitmentProcess " + id + " not found"
                ));

        if (!recruitmentProcess.isDeleted()) {
            throw new InvalidEntityStateException(
                    "RecruitmentProcess " + id + " is not deleted"
            );
        }

        recruitmentProcess.setDeleted(false);
    }
}
