package com.szponty.recruitment_system.attachment.service;

import com.szponty.recruitment_system.attachment.dto.CreateAttachmentRequest;
import com.szponty.recruitment_system.attachment.dto.InitiateUploadResponse;
import com.szponty.recruitment_system.attachment.model.Attachment;
import com.szponty.recruitment_system.attachment.model.AttachmentStatus;
import com.szponty.recruitment_system.attachment.repository.AttachmentRepository;
import com.szponty.recruitment_system.common.exception.BadRequestException;
import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.jobApplication.repository.JobApplicationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;

@Service
public class AttachmentService {
    private static final Duration UPLOAD_URL_TTL = Duration.ofMinutes(5);
    private static final Duration DOWNLOAD_URL_TTL = Duration.ofMinutes(15);

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    );
    private static final long MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB

    private final AttachmentRepository attachmentRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final StorageService storageService;

    private final String bucket;

    public AttachmentService(
            AttachmentRepository attachmentRepository,
            JobApplicationRepository jobApplicationRepository,
            StorageService storageService,
            @Value("${app.s3.bucket}") String bucket
    ) {
        this.attachmentRepository = attachmentRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.storageService = storageService;
        this.bucket = bucket;
    }

    @Transactional
    public InitiateUploadResponse initiateUpload(UUID jobApplicationId, CreateAttachmentRequest request) {
        validateFileMetadata(request);

        JobApplication jobApplication = jobApplicationRepository.getOrThrow(jobApplicationId, "JobApplication");

        String key = jobApplicationId + "/" + UUID.randomUUID() + extensionOf(request.originalName());

        Attachment attachment = Attachment.builder()
                .jobApplication(jobApplication)
                .originalName(request.originalName())
                .storedName(key)
                .path(bucket)
                .contentType(request.contentType())
                .sizeBytes(request.sizeBytes())
                .cv(request.isCv())
                .status(AttachmentStatus.PENDING)
                .build();

        Attachment saved = attachmentRepository.save(attachment);
        URL uploadUrl = storageService.generateUploadUrl(key, UPLOAD_URL_TTL);

        return new InitiateUploadResponse(saved.getId(), uploadUrl);
    }

    @Transactional(readOnly = true)
    public URL getDownloadUrl(UUID attachmentId) {
        Attachment attachment = attachmentRepository.getOrThrow(attachmentId, "Attachment");

        if (attachment.getStatus() != AttachmentStatus.ACTIVE) {
            throw new InvalidEntityStateException(
                    "Attachment " + attachmentId + " is not active"
            );
        }

        return storageService.generateDownloadUrl(attachment.getStoredName(), DOWNLOAD_URL_TTL);
    }

    @Transactional
    public void confirmUpload(UUID attachmentId) {
        Attachment attachment = attachmentRepository.getOrThrow(attachmentId, "Attachment");

        if (attachment.getStatus() != AttachmentStatus.PENDING) {
            throw new InvalidEntityStateException(
                    "Attachment " + attachmentId + " is not pending upload"
            );
        }

        attachment.setStatus(AttachmentStatus.ACTIVE);
    }

    private void validateFileMetadata(CreateAttachmentRequest request) {
        if (!ALLOWED_CONTENT_TYPES.contains(request.contentType())) {
            throw new BadRequestException("Unsupported content type: " + request.contentType());
        }

        if (request.sizeBytes() == null || request.sizeBytes() <= 0) {
            throw new BadRequestException("File size must be greater than zero");
        }

        if (request.sizeBytes() > MAX_FILE_SIZE_BYTES) {
            throw new BadRequestException(
                    "File exceeds maximum allowed size of " + MAX_FILE_SIZE_BYTES + " bytes"
            );
        }
    }

    private String extensionOf(String originalName) {
        int dotIndex = originalName.lastIndexOf('.');
        return dotIndex == -1 ? "" : originalName.substring(dotIndex);
    }
}
