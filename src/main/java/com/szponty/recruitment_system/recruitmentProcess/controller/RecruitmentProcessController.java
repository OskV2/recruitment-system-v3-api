package com.szponty.recruitment_system.recruitmentProcess.controller;


import com.szponty.recruitment_system.recruitmentProcess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentProcess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentProcess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentProcess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentProcess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentProcess.service.ProcessStepService;
import com.szponty.recruitment_system.recruitmentProcess.service.RecruitmentProcessService;
import com.szponty.recruitment_system.recruitmentProcess.service.RecruitmentProcessVersionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruitment-processes")
public class RecruitmentProcessController {
    private final ProcessStepService processStepService;
    private final RecruitmentProcessService recruitmentProcessService;
    private final RecruitmentProcessVersionService recruitmentProcessVersionService;

    @GetMapping
    public ResponseEntity<List<RecruitmentProcessResponse>> getAllRecruitmentProcesses() {
        return ResponseEntity.ok().body(recruitmentProcessService.getAllRecruitmentProcesses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentProcessResponse> getRecruitmentProcessById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(recruitmentProcessService.getRecruitmentProcessById(id));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<RecruitmentProcessVersionResponse>> getAllRecruitmentProcessVersionsByRecruitmentProcessId(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok().body(
                recruitmentProcessVersionService.getAllRecruitmentProcVersionsByRecruitmentProcessId(id)
        );
    }

    @PostMapping
    public ResponseEntity<RecruitmentProcessResponse> createRecruitmentProcess(
            @Valid @RequestBody CreateRecruitmentProcessRequest request
    ) {
        return ResponseEntity.ok().body(recruitmentProcessService.createRecruitmentProcess(request));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRecruitmentProcess(
            @PathVariable UUID id
    ) {
        recruitmentProcessService.restoreRecruitmentProcess(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentProcess(
            @PathVariable UUID id
    ) {
        recruitmentProcessService.deleteRecruitmentProcess(id);
        return ResponseEntity.noContent().build();
    }
}
