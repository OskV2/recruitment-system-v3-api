package com.szponty.recruitment_system.attachment.repository;

import com.szponty.recruitment_system.attachment.model.Attachment;
import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;

import java.util.List;
import java.util.UUID;

public interface AttachmentRepository extends FindOrThrowRepository<Attachment, UUID> {
    List<Attachment> findByJobApplicationId(UUID jobApplicationId);
}
