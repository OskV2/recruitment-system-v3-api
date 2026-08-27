package com.szponty.recruitment_system.interview.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateInterviewRequest(
    UUID recruiterId,
    LocalDateTime scheduledStart,
    LocalDateTime scheduledEnd,
    String location,
    String meetingUrl,
    String notes
) {
}
