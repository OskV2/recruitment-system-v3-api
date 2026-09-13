package com.szponty.recruitment_system.attachment.event;

import com.szponty.recruitment_system.attachment.model.Attachment;
import com.szponty.recruitment_system.attachment.repository.AttachmentRepository;
import com.szponty.recruitment_system.attachment.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttachmentEventListenerTest {

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private StorageService storageService;

    private AttachmentEventListener listener;

    @BeforeEach
    void setUp() {
        listener = new AttachmentEventListener(attachmentRepository, storageService);
    }

    @Test
    void shouldExtractTextAndSaveOnAttachment() {
        UUID attachmentId = UUID.randomUUID();
        String key = "some/key.txt";
        Attachment attachment = Attachment.builder().id(attachmentId).build();

        InputStream fileContent = new ByteArrayInputStream(
                "Hello Tika test".getBytes(StandardCharsets.UTF_8)
        );

        when(storageService.downloadObject(key)).thenReturn(fileContent);
        when(attachmentRepository.getOrThrow(attachmentId, "Attachment")).thenReturn(attachment);

        listener.onAttachmentUploaded(new AttachmentUploadedEvent(attachmentId, key));

        assertNotNull(attachment.getExtractedText());
        assertTrue(attachment.getExtractedText().contains("Hello Tika test"));
    }

    @Test
    void shouldNotThrowWhenDownloadFails() {
        UUID attachmentId = UUID.randomUUID();
        String key = "some/key.txt";

        when(storageService.downloadObject(key)).thenThrow(new RuntimeException("S3 unreachable"));

        assertDoesNotThrow(() ->
                listener.onAttachmentUploaded(new AttachmentUploadedEvent(attachmentId, key))
        );

        verify(attachmentRepository, never()).getOrThrow(any(), any());
    }
}
