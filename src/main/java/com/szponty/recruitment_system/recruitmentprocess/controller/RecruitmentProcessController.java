package com.szponty.recruitment_system.recruitmentprocess.controller;


import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentprocess.DTO.UpdateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentprocess.service.ProcessStepService;
import com.szponty.recruitment_system.recruitmentprocess.service.RecruitmentProcessService;
import com.szponty.recruitment_system.recruitmentprocess.service.RecruitmentProcessVersionService;
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

    private final RecruitmentProcessService recruitmentProcessService;
    private final RecruitmentProcessVersionService recruitmentProcessVersionService;

    @GetMapping
    public ResponseEntity<List<RecruitmentProcessResponse>> getAllRecruitmentProcesses() {
        return ResponseEntity.ok(recruitmentProcessService.getAllRecruitmentProcesses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<RecruitmentProcessResponse>> getRecruitmentProcessById(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "true") boolean includeSteps,
            @RequestParam(defaultValue = "latest") String version
    ) {
        return ResponseEntity.ok(
                recruitmentProcessService.getRecruitmentProcessById(id, version, includeSteps)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecruitmentProcessResponse> updateRecruitmentProcess(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateRecruitmentProcessRequest request
    ) {
        return ResponseEntity.ok(recruitmentProcessService.updateRecruitmentProcess(id, request));
    }

//    @PostMapping()
//    public ResponseEntity<RecruitmentProcessResponse> createRecruitmentProcess(
//            @Valid @RequestBody CreateRecruitmentProcessRequest request
//    ) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(recruitmentProcessService.updateRecruitmentProcess(id, request));
//    }

    @PatchMapping("/{id}/activate/{versionId}")
    public ResponseEntity<Void> activateVersion(@PathVariable UUID id, @PathVariable UUID versionId) {
        recruitmentProcessService.activateVersion(id, versionId);
        return  ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRecruitmentProcess(@PathVariable UUID id) {
        recruitmentProcessService.restoreRecruitmentProcess(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruitmentProcess(@PathVariable UUID id) {
        recruitmentProcessService.deleteRecruitmentProcess(id);
        return ResponseEntity.noContent().build();
    }
}
