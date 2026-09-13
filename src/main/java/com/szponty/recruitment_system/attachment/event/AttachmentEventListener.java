package com.szponty.recruitment_system.attachment.event;

import com.szponty.recruitment_system.attachment.model.Attachment;
import com.szponty.recruitment_system.attachment.repository.AttachmentRepository;
import com.szponty.recruitment_system.attachment.service.StorageService;
import org.apache.tika.Tika;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.InputStream;

@Component
public class AttachmentEventListener {
    private static final Logger log = LoggerFactory.getLogger(AttachmentEventListener.class);

    private final AttachmentRepository attachmentRepository;
    private final StorageService storageService;
    private final Tika tika = new Tika();

    public AttachmentEventListener(AttachmentRepository attachmentRepository, StorageService storageService) {
        this.attachmentRepository = attachmentRepository;
        this.storageService = storageService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onAttachmentUploaded(AttachmentUploadedEvent event) {
        try (InputStream inputStream = storageService.downloadObject(event.storedName())) {
            String text = tika.parseToString(inputStream);

            Attachment attachment = attachmentRepository.getOrThrow(event.attachmentId(), "Attachment");
            attachment.setExtractedText(text);
        } catch (Exception e) {
            log.error("Failed to extract text for attachment {}", event.attachmentId(), e);
        }
    }
}
