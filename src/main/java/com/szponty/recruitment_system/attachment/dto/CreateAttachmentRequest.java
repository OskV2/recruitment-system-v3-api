package com.szponty.recruitment_system.attachment.dto;

public record CreateAttachmentRequest(
        String originalName,
        String contentType,
        Long sizeBytes,
        boolean isCv
) {}
