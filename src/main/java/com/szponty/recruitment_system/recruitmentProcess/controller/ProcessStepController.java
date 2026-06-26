package com.szponty.recruitment_system.recruitmentProcess.controller;

import com.szponty.recruitment_system.recruitmentProcess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentProcess.DTO.UpdateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentProcess.service.ProcessStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/steps")
public class ProcessStepController {
    private final ProcessStepService service;

    @PatchMapping("/{id}")
    public ResponseEntity<ProcessStepResponse> updateProcessStep(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProcessStepRequest request
    ) {
        return ResponseEntity.ok(service.updateProcessStep(id, request));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreProcessStep(
            @PathVariable UUID id
    ) {
        service.restoreProcessStep(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessStep(
            @PathVariable UUID id
    ) {
        service.deleteProcessStep(id);
        return ResponseEntity.noContent().build();
    }
}
