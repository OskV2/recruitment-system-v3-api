package com.szponty.recruitment_system.attachment.event;

import java.util.UUID;

public record AttachmentUploadedEvent(UUID attachmentId, String storedName) {}
