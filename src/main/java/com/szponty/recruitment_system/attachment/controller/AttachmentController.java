package com.szponty.recruitment_system.attachment.controller;

import com.szponty.recruitment_system.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmUpload(@PathVariable UUID id) {
        attachmentService.confirmUpload(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download-url")
    public ResponseEntity<URL> getDownloadUrl(@PathVariable UUID id) {
        return ResponseEntity.ok(attachmentService.getDownloadUrl(id));
    }
}
