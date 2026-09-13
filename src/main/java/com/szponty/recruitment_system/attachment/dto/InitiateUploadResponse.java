package com.szponty.recruitment_system.attachment.dto;

import java.net.URL;
import java.util.UUID;

public record InitiateUploadResponse(UUID attachmentId, URL uploadUrl) {}
