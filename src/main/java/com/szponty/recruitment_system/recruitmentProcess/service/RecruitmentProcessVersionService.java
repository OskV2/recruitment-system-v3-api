package com.szponty.recruitment_system.recruitmentProcess.service;

import com.szponty.recruitment_system.recruitmentProcess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentProcess.mapper.RecruitmentProcessVersionMapper;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentProcess.repository.RecruitmentProcessRepository;
import com.szponty.recruitment_system.recruitmentProcess.repository.RecruitmentProcessVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessVersionService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;
    private final RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;

    private final RecruitmentProcessVersionMapper recruitmentProcessVersionMapper;

    public List<RecruitmentProcessVersionResponse> getAllRecruitmentProcVersionsByRecruitmentProcessId(UUID id) {
        RecruitmentProcess recruitmentProcess = recruitmentProcessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Recruitment process with id " + id + " was not found."
                ));

        return recruitmentProcessVersionRepository.findByRecruitmentProcess(recruitmentProcess)
                        .stream()
                        .map(recruitmentProcessVersionMapper::toResponse)
                        .toList();
    }
}
