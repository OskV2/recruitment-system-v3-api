package com.szponty.recruitment_system.interview.DTO;

import com.szponty.recruitment_system.interview.model.InterviewStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateInterviewRequest(
    UUID recruiterId,
    LocalDateTime scheduledStart,
    LocalDateTime scheduledEnd,
    String location,
    String meetingUrl,
    InterviewStatus status
) {
}
