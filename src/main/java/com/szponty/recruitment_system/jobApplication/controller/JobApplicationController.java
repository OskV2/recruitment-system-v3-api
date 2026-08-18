package com.szponty.recruitment_system.jobApplication.controller;

import com.szponty.recruitment_system.interview.dto.InterviewResponse;
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

    @GetMapping("/{id}/interviews")
    public ResponseEntity<List<InterviewResponse>> getAllInterviewsForJobApplication(@PathVariable UUID id) {
        List<InterviewResponse> interviews = interviewService.getInterviewsForJobApplication(id);
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/{id}/interviews/{interview-id}")
    public ResponseEntity<InterviewResponse> getInterviewForJobApplication(@PathVariable UUID id, @PathVariable("interview-id") UUID interviewId) {
        InterviewResponse interview = interviewService.getInterviewByIdAndJobApplicationId(id, interviewId);
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
