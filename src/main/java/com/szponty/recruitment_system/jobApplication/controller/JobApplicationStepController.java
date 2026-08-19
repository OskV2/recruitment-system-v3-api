package com.szponty.recruitment_system.jobApplication.controller;


import com.szponty.recruitment_system.interview.DTO.CreateInterviewRequest;
import com.szponty.recruitment_system.interview.DTO.InterviewResponse;
import com.szponty.recruitment_system.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-application-steps")
public class JobApplicationStepController {

    private final InterviewService interviewService;

    @PostMapping("/{stepId}/interviews")
    public ResponseEntity<InterviewResponse> createInterview(
            @PathVariable UUID stepId,
            @RequestBody CreateInterviewRequest request
    ) {
        InterviewResponse response = interviewService.createInterview(stepId, request);
        return ResponseEntity.ok(response);
    }
}
