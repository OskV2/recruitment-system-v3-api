package com.szponty.recruitment_system.jobApplication.controller;

import com.szponty.recruitment_system.attachment.dto.CreateAttachmentRequest;
import com.szponty.recruitment_system.attachment.dto.InitiateUploadResponse;
import com.szponty.recruitment_system.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-applications")
public class JobApplicationController {
    private final AttachmentService attachmentService;

    @PostMapping("/{jobApplicationId}/attachments/upload-url")
    public ResponseEntity<InitiateUploadResponse> initiateAttachmentUpload(
            @PathVariable UUID jobApplicationId,
            @RequestBody CreateAttachmentRequest request
    ) {
        return ResponseEntity.ok(attachmentService.initiateUpload(jobApplicationId, request));
    }
}
