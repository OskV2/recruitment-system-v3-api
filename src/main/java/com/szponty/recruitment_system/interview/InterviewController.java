package com.szponty.recruitment_system.interview;

import com.szponty.recruitment_system.auth.service.LoggedUserService;
import com.szponty.recruitment_system.interview.DTO.InterviewResponse;
import com.szponty.recruitment_system.interview.model.InterviewStatus;
import com.szponty.recruitment_system.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService interviewService;
    private final LoggedUserService loggedUserService;

    @GetMapping
    public ResponseEntity<List<InterviewResponse>> getAllInterviews(
            @RequestParam(required = false) InterviewStatus status
    ) {
        UUID recruiterId = loggedUserService.getCurrentUser().getId();

        List<InterviewResponse> interviews = interviewService.getAssignedInterviews(
                recruiterId,
                Optional.ofNullable(status)
        );

        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InterviewResponse> getInterviewById(@PathVariable UUID id) {
        return ResponseEntity.ok(interviewService.getInterviewById(id));
    }
}
