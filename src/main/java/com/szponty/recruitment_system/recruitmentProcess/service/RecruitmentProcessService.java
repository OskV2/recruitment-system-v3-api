package com.szponty.recruitment_system.recruitmentProcess.service;

import com.szponty.recruitment_system.recruitmentProcess.repository.RecruitmentProcessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentProcessService {
    private final RecruitmentProcessRepository recruitmentProcessRepository;


}
