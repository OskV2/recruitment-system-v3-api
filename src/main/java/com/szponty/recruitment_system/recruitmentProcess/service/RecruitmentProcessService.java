package com.szponty.recruitment_system.recruitmentProcess.service;

import com.szponty.recruitment_system.recruitmentProcess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentProcess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentProcess.mapper.RecruitmentProcessMapper;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentProcess.repository.RecruitmentProcessRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessMapper processMapper;


    public RecruitmentProcessResponse getRecruitmentProcessById(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "RecruitmentProcess " + id + " not found"
                ));

        return processMapper.toResponse(recruitmentProcess);
    }

    public RecruitmentProcessResponse createRecruitmentProcess(CreateRecruitmentProcessRequest request) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.save(processMapper.toEntity(request));
        return processMapper.toResponse(recruitmentProcess);
    }

    @Transactional
    public void deleteRecruitmentProcess(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "RecruitmentProcess " + id + " not found"
                ));

        if (recruitmentProcess.isDeleted()) {
            throw new IllegalArgumentException(
                    "RecruitmentProcess " + id + " already deleted"
            );
        }

        recruitmentProcess.setDeleted(true);
    }

    public void restoreRecruitmentProcess(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "RecruitmentProcess " + id + " not found"
                ));

        if (!recruitmentProcess.isDeleted()) {
            throw new IllegalArgumentException(
                    "RecruitmentProcess " + id + " is not deleted"
            );
        }

        recruitmentProcess.setDeleted(false);
    }
}
