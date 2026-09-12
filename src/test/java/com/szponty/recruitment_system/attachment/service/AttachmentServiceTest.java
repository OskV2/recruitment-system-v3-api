package com.szponty.recruitment_system.attachment.service;

import com.szponty.recruitment_system.attachment.dto.CreateAttachmentRequest;
import com.szponty.recruitment_system.attachment.dto.InitiateUploadResponse;
import com.szponty.recruitment_system.attachment.model.Attachment;
import com.szponty.recruitment_system.attachment.model.AttachmentStatus;
import com.szponty.recruitment_system.attachment.repository.AttachmentRepository;
import com.szponty.recruitment_system.common.exception.BadRequestException;
import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.ResourceNotFoundException;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.jobApplication.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttachmentServiceTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private AttachmentService attachmentService;

    @BeforeEach
    void setUp() {
        attachmentService = new AttachmentService(
                attachmentRepository, jobApplicationRepository, storageService, eventPublisher, "test-bucket"
        );
    }

    @Test
    void shouldInitiateUpload() throws MalformedURLException {
        UUID jobApplicationId = UUID.randomUUID();
        JobApplication jobApplication = JobApplication.builder().id(jobApplicationId).build();

        CreateAttachmentRequest request = new CreateAttachmentRequest(
                "cv.pdf", "application/pdf", 1000L, true
        );

        when(jobApplicationRepository.getOrThrow(jobApplicationId, "JobApplication"))
                .thenReturn(jobApplication);
        when(attachmentRepository.save(any(Attachment.class)))
                .thenAnswer(invocation -> {
                    Attachment attachment = invocation.getArgument(0);
                    attachment.setId(UUID.randomUUID());
                    return attachment;
                });
        when(storageService.generateUploadUrl(anyString(), any(Duration.class)))
                .thenReturn(URI.create("http://localhost:8333/fake-url").toURL());

        InitiateUploadResponse result = attachmentService.initiateUpload(jobApplicationId, request);

        assertNotNull(result.attachmentId());
        assertNotNull(result.uploadUrl());

        verify(attachmentRepository).save(any(Attachment.class));
    }

    @Test
    void shouldThrowWhenContentTypeNotAllowed() {
        UUID jobApplicationId = UUID.randomUUID();
        CreateAttachmentRequest request = new CreateAttachmentRequest(
                "virus.exe", "application/x-msdownload", 1000L, true
        );

        assertThrows(
                BadRequestException.class,
                () -> attachmentService.initiateUpload(jobApplicationId, request)
        );

        verify(attachmentRepository, never()).save(any());
        verify(jobApplicationRepository, never()).getOrThrow(any(), any());
    }

    @Test
    void shouldThrowWhenSizeBytesIsNull() {
        UUID jobApplicationId = UUID.randomUUID();
        CreateAttachmentRequest request = new CreateAttachmentRequest(
                "cv.pdf", "application/pdf", null, true
        );

        assertThrows(
                BadRequestException.class,
                () -> attachmentService.initiateUpload(jobApplicationId, request)
        );

        verify(attachmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenSizeBytesIsZeroOrNegative() {
        UUID jobApplicationId = UUID.randomUUID();
        CreateAttachmentRequest request = new CreateAttachmentRequest(
                "cv.pdf", "application/pdf", 0L, true
        );

        assertThrows(
                BadRequestException.class,
                () -> attachmentService.initiateUpload(jobApplicationId, request)
        );

        verify(attachmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenFileExceedsMaxSize() {
        UUID jobApplicationId = UUID.randomUUID();
        CreateAttachmentRequest request = new CreateAttachmentRequest(
                "cv.pdf", "application/pdf", 11L * 1024 * 1024, true
        );

        assertThrows(
                BadRequestException.class,
                () -> attachmentService.initiateUpload(jobApplicationId, request)
        );

        verify(attachmentRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenJobApplicationNotFoundOnInitiateUpload() {
        UUID jobApplicationId = UUID.randomUUID();
        CreateAttachmentRequest request = new CreateAttachmentRequest(
                "cv.pdf", "application/pdf", 1000L, true
        );

        when(jobApplicationRepository.getOrThrow(jobApplicationId, "JobApplication"))
                .thenThrow(new ResourceNotFoundException("JobApplication", jobApplicationId));

        assertThrows(
                ResourceNotFoundException.class,
                () -> attachmentService.initiateUpload(jobApplicationId, request)
        );

        verify(attachmentRepository, never()).save(any());
    }

    @Test
    void shouldConfirmUpload() {
        UUID attachmentId = UUID.randomUUID();
        Attachment attachment = attachmentWithStatus(attachmentId, AttachmentStatus.PENDING);

        when(attachmentRepository.getOrThrow(attachmentId, "Attachment")).thenReturn(attachment);

        attachmentService.confirmUpload(attachmentId);

        assertEquals(AttachmentStatus.ACTIVE, attachment.getStatus());
    }

    @Test
    void shouldThrowWhenConfirmingNonPendingAttachment() {
        UUID attachmentId = UUID.randomUUID();
        Attachment attachment = attachmentWithStatus(attachmentId, AttachmentStatus.ACTIVE);

        when(attachmentRepository.getOrThrow(attachmentId, "Attachment")).thenReturn(attachment);

        assertThrows(
                InvalidEntityStateException.class,
                () -> attachmentService.confirmUpload(attachmentId)
        );
    }

    @Test
    void shouldThrowWhenConfirmingNonExistentAttachment() {
        UUID attachmentId = UUID.randomUUID();

        when(attachmentRepository.getOrThrow(attachmentId, "Attachment"))
                .thenThrow(new ResourceNotFoundException("Attachment", attachmentId));

        assertThrows(
                ResourceNotFoundException.class,
                () -> attachmentService.confirmUpload(attachmentId)
        );
    }

    @Test
    void shouldGetDownloadUrl() throws MalformedURLException {
        UUID attachmentId = UUID.randomUUID();
        Attachment attachment = attachmentWithStatus(attachmentId, AttachmentStatus.ACTIVE);
        attachment.setStoredName("some/key.pdf");

        URL expectedUrl = URI.create("http://localhost:8333/fake-download-url").toURL();

        when(attachmentRepository.getOrThrow(attachmentId, "Attachment")).thenReturn(attachment);
        when(storageService.generateDownloadUrl(eq("some/key.pdf"), any(Duration.class)))
                .thenReturn(expectedUrl);

        URL result = attachmentService.getDownloadUrl(attachmentId);

        assertEquals(expectedUrl, result);
    }

    @Test
    void shouldThrowWhenDownloadingNonActiveAttachment() {
        UUID attachmentId = UUID.randomUUID();
        Attachment attachment = attachmentWithStatus(attachmentId, AttachmentStatus.PENDING);

        when(attachmentRepository.getOrThrow(attachmentId, "Attachment")).thenReturn(attachment);

        assertThrows(
                InvalidEntityStateException.class,
                () -> attachmentService.getDownloadUrl(attachmentId)
        );
    }

    @Test
    void shouldThrowWhenDownloadingNonExistentAttachment() {
        UUID attachmentId = UUID.randomUUID();

        when(attachmentRepository.getOrThrow(attachmentId, "Attachment"))
                .thenThrow(new ResourceNotFoundException("Attachment", attachmentId));

        assertThrows(
                ResourceNotFoundException.class,
                () -> attachmentService.getDownloadUrl(attachmentId)
        );
    }

    private Attachment attachmentWithStatus(UUID id, AttachmentStatus status) {
        return Attachment.builder()
                .id(id)
                .status(status)
                .build();
    }
}
