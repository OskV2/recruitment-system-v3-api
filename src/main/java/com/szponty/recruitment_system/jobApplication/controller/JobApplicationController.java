package com.szponty.recruitment_system.jobApplication.controller;

import com.szponty.recruitment_system.interview.DTO.InterviewResponse;
import com.szponty.recruitment_system.interview.DTO.UpdateInterviewRequest;
import com.szponty.recruitment_system.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-applications")
public class JobApplicationController {

    private final InterviewService interviewService;

    @GetMapping("/{jobApplicationId}/interviews")
    public ResponseEntity<List<InterviewResponse>> getAllInterviewsForJobApplication(@PathVariable UUID jobApplicationId) {
        List<InterviewResponse> interviews = interviewService.getInterviewsForJobApplication(jobApplicationId);
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/{jobApplicationId}/interviews/{interviewId}")
    public ResponseEntity<InterviewResponse> getInterviewForJobApplication(@PathVariable UUID jobApplicationId, @PathVariable UUID interviewId) {
        InterviewResponse interview = interviewService.getInterviewByIdAndJobApplicationId(jobApplicationId, interviewId);
        return ResponseEntity.ok(interview);
    }

    @PatchMapping("/{jobApplicationId}/interviews/{interviewId}")
    public ResponseEntity<InterviewResponse> updateInterviewDetails(
            @PathVariable UUID jobApplicationId,
            @PathVariable UUID interviewId,
            @RequestBody UpdateInterviewRequest request
    ) {
        InterviewResponse interview = interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);
        return ResponseEntity.ok(interview);
    }

    @DeleteMapping("/{jobApplicationId}/interviews/{interviewId}")
    public ResponseEntity<Void> deleteInterview(@PathVariable UUID jobApplicationId, @PathVariable UUID interviewId) {
        interviewService.deleteInterview(jobApplicationId, interviewId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{jobApplicationId}/interviews/{interviewId}/restore")
    public ResponseEntity<Void> restoreInterview(@PathVariable UUID jobApplicationId, @PathVariable UUID interviewId) {
        interviewService.restoreInterview(jobApplicationId, interviewId);
        return ResponseEntity.noContent().build();
    }
}
