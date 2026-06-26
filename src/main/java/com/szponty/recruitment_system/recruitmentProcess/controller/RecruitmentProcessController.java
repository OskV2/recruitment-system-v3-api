package com.szponty.recruitment_system.recruitmentProcess.controller;


import com.szponty.recruitment_system.recruitmentProcess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentProcess.service.ProcessStepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruitment-process")
public class RecruitmentProcessController {
    private final ProcessStepService processStepService;

    @GetMapping("/{id}/steps")
    public ResponseEntity<List<ProcessStepResponse>> getAllStepsByRecruitmentProcessId(@PathVariable UUID id) {
        return ResponseEntity.ok(processStepService.getProcessStepsByRecruitmentProcessVersionId(id));
    }
}
