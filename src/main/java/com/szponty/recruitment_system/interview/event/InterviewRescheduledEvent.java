package com.szponty.recruitment_system.interview.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record InterviewRescheduledEvent(
        UUID interviewId,
        UUID jobApplicationId,
        LocalDateTime oldScheduledStart,
        LocalDateTime newScheduledStart,
        LocalDateTime oldScheduledEnd,
        LocalDateTime newScheduledEnd,
        String oldLocation,
        String newLocation,
        String oldMeetingUrl,
        String newMeetingUrl
) {
}
