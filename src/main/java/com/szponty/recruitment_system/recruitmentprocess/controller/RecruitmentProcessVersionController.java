package com.szponty.recruitment_system.recruitmentprocess.controller;


import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessVersionRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentprocess.service.RecruitmentProcessVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recruitment-process-versions")
public class RecruitmentProcessVersionController {

    private final RecruitmentProcessVersionService recruitmentProcessVersionService;

    @GetMapping("/{id}")
    public ResponseEntity<RecruitmentProcessVersionResponse> getRecruitmentProcessVersionById(@PathVariable UUID id) {
        return ResponseEntity.ok(recruitmentProcessVersionService.getRecruitmentProcessVersionById(id));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateRecruitmentProcessVersion(@PathVariable UUID id) {
        recruitmentProcessVersionService.activateRecruitmentProcessVersion(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/inactivate")
    public ResponseEntity<Void> inactivateRecruitmentProcessVersion(@PathVariable UUID id) {
        recruitmentProcessVersionService.inactivateRecruitmentProcessVersion(id);
        return ResponseEntity.noContent().build();
    }
}
