package com.szponty.recruitment_system.recruitmentProcess.controller;

import com.szponty.recruitment_system.recruitmentProcess.DTO.CreateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentProcess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentProcess.DTO.UpdateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentProcess.service.ProcessStepService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/steps")
public class ProcessStepController {
    private final ProcessStepService processStepService;

    @GetMapping
    public ResponseEntity<List<ProcessStepResponse>> getProcessSteps(                                  
            @RequestParam(defaultValue = "false") boolean deleted
    ) {
        return ResponseEntity.ok(processStepService.getAllSteps(deleted));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcessStepResponse> getProcessStepById(@PathVariable UUID id) {
        return ResponseEntity.ok(processStepService.getProcessStepById(id));
    }

    @PostMapping
    public ResponseEntity<ProcessStepResponse> createProcessStep(
            @RequestBody @Valid CreateProcessStepRequest request
    ) {
        ProcessStepResponse response = processStepService.createProcessStep(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProcessStepResponse> updateProcessStep(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProcessStepRequest request
    ) {
        return ResponseEntity.ok(processStepService.updateProcessStep(id, request));
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreProcessStep(
            @PathVariable UUID id
    ) {
        processStepService.restoreProcessStep(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcessStep(
            @PathVariable UUID id
    ) {
        processStepService.deleteProcessStep(id);
        return ResponseEntity.noContent().build();
    }
}
