package com.szponty.recruitment_system.interview.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.ResourceNotFoundException;
import com.szponty.recruitment_system.interview.DTO.CreateInterviewRequest;
import com.szponty.recruitment_system.interview.DTO.InterviewResponse;
import com.szponty.recruitment_system.interview.DTO.UpdateInterviewRequest;
import com.szponty.recruitment_system.interview.event.InterviewRescheduledEvent;
import com.szponty.recruitment_system.interview.mapper.InterviewMapper;
import com.szponty.recruitment_system.interview.model.Interview;
import com.szponty.recruitment_system.interview.model.InterviewStatus;
import com.szponty.recruitment_system.interview.repository.InterviewRepository;
import com.szponty.recruitment_system.jobApplication.model.JobApplication;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStep;
import com.szponty.recruitment_system.jobApplication.model.JobApplicationStepStatus;
import com.szponty.recruitment_system.jobApplication.repository.JobApplicationStepRepository;
import com.szponty.recruitment_system.user.model.User;
import com.szponty.recruitment_system.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Fully vibe-coded

@ExtendWith(MockitoExtension.class)
class InterviewServiceTest {

    @Mock
    private InterviewRepository interviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobApplicationStepRepository jobApplicationStepRepository;

    @Mock
    private InterviewMapper interviewMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InterviewService interviewService;

    private final InterviewResponse dummyResponse = new InterviewResponse(
            UUID.randomUUID(), null, null, null, null, null, null, null, null, false, null, null
    );

    private Interview scheduledInterview(UUID id, LocalDateTime start, LocalDateTime end) {
        return Interview.builder()
                .id(id)
                .scheduledStart(start)
                .scheduledEnd(end)
                .status(InterviewStatus.SCHEDULED)
                .location("Room 1")
                .meetingUrl("https://meet.example.com/old")
                .deleted(false)
                .build();
    }

    // ---------- getInterviewById ----------

    @Test
    void shouldGetInterviewById() {
        UUID id = UUID.randomUUID();
        Interview interview = scheduledInterview(id, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(interviewRepository.getOrThrow(id, "Interview")).thenReturn(interview);
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        InterviewResponse result = interviewService.getInterviewById(id);

        assertEquals(dummyResponse, result);
    }

    @Test
    void shouldThrowWhenInterviewByIdNotFound() {
        UUID id = UUID.randomUUID();

        when(interviewRepository.getOrThrow(id, "Interview"))
                .thenThrow(new ResourceNotFoundException("Interview", id));

        assertThrows(ResourceNotFoundException.class, () -> interviewService.getInterviewById(id));
    }

    // ---------- getInterviewByIdAndJobApplicationId ----------

    @Test
    void shouldGetInterviewByIdAndJobApplicationId() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        Interview interview = scheduledInterview(interviewId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        InterviewResponse result = interviewService.getInterviewByIdAndJobApplicationId(jobApplicationId, interviewId);

        assertEquals(dummyResponse, result);
    }

    @Test
    void shouldThrowWhenInterviewDoesNotBelongToJobApplication() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.getInterviewByIdAndJobApplicationId(jobApplicationId, interviewId)
        );
    }

    // ---------- getInterviewsForJobApplication ----------

    @Test
    void shouldGetAllInterviewsForJobApplication() {
        UUID jobApplicationId = UUID.randomUUID();
        Interview interview = scheduledInterview(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(interviewRepository.findByJobApplicationId(jobApplicationId))
                .thenReturn(List.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        List<InterviewResponse> result = interviewService.getInterviewsForJobApplication(jobApplicationId);

        assertEquals(1, result.size());
        assertEquals(dummyResponse, result.getFirst());
    }

    // ---------- getAssignedInterviews ----------

    @Test
    void shouldGetAssignedInterviewsWithoutStatusFilter() {
        UUID recruiterId = UUID.randomUUID();
        Interview interview = scheduledInterview(UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(userRepository.findById(recruiterId)).thenReturn(Optional.of(User.builder().id(recruiterId).build()));
        when(interviewRepository.findByRecruiterIdAndOptionalStatus(recruiterId, null))
                .thenReturn(List.of(interview));
        when(interviewMapper.toResponseList(List.of(interview))).thenReturn(List.of(dummyResponse));

        List<InterviewResponse> result = interviewService.getAssignedInterviews(recruiterId, Optional.empty());

        assertEquals(1, result.size());
        verify(interviewRepository).findByRecruiterIdAndOptionalStatus(recruiterId, null);
    }

    @Test
    void shouldGetAssignedInterviewsFilteredByStatus() {
        UUID recruiterId = UUID.randomUUID();

        when(userRepository.findById(recruiterId)).thenReturn(Optional.of(User.builder().id(recruiterId).build()));
        when(interviewRepository.findByRecruiterIdAndOptionalStatus(recruiterId, InterviewStatus.CANCELLED))
                .thenReturn(List.of());
        when(interviewMapper.toResponseList(List.of())).thenReturn(List.of());

        List<InterviewResponse> result = interviewService.getAssignedInterviews(
                recruiterId, Optional.of(InterviewStatus.CANCELLED)
        );

        assertTrue(result.isEmpty());
        verify(interviewRepository).findByRecruiterIdAndOptionalStatus(recruiterId, InterviewStatus.CANCELLED);
    }

    @Test
    void shouldThrowWhenRecruiterDoesNotExistForAssignedInterviews() {
        UUID recruiterId = UUID.randomUUID();

        when(userRepository.findById(recruiterId)).thenReturn(Optional.empty());

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.getAssignedInterviews(recruiterId, Optional.empty())
        );

        verify(interviewRepository, never()).findByRecruiterIdAndOptionalStatus(any(), any());
    }

    // ---------- createInterview ----------

    @Test
    void shouldCreateInterview() {
        UUID stepId = UUID.randomUUID();
        UUID recruiterId = UUID.randomUUID();
        JobApplication jobApplication = JobApplication.builder().id(UUID.randomUUID()).build();
        JobApplicationStep step = JobApplicationStep.builder()
                .id(stepId)
                .jobApplication(jobApplication)
                .status(JobApplicationStepStatus.WAITING)
                .build();
        User recruiter = User.builder().id(recruiterId).build();

        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);
        CreateInterviewRequest request = new CreateInterviewRequest(
                recruiterId, start, end, "Room 2", "https://meet.example.com/new", "notes"
        );

        when(jobApplicationStepRepository.getOrThrow(stepId, "JobApplicationStep")).thenReturn(step);
        when(interviewRepository.existsByJobApplicationStepId(stepId)).thenReturn(false);
        when(userRepository.getOrThrow(recruiterId, "User")).thenReturn(recruiter);
        when(interviewRepository.save(any(Interview.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(interviewMapper.toResponse(any(Interview.class))).thenReturn(dummyResponse);

        InterviewResponse result = interviewService.createInterview(stepId, request);

        assertEquals(dummyResponse, result);

        ArgumentCaptor<Interview> captor = ArgumentCaptor.forClass(Interview.class);
        verify(interviewRepository).save(captor.capture());
        Interview saved = captor.getValue();

        assertEquals(jobApplication, saved.getJobApplication());
        assertEquals(step, saved.getJobApplicationStep());
        assertEquals(recruiter, saved.getRecruiter());
        assertEquals(start, saved.getScheduledStart());
        assertEquals(end, saved.getScheduledEnd());
        assertEquals("Room 2", saved.getLocation());
        assertEquals("https://meet.example.com/new", saved.getMeetingUrl());
        assertEquals("notes", saved.getNotes());
        assertEquals(InterviewStatus.SCHEDULED, saved.getStatus());
    }

    @Test
    void shouldThrowWhenCreatingInterviewForStepNotWaiting() {
        UUID stepId = UUID.randomUUID();
        JobApplicationStep step = JobApplicationStep.builder()
                .id(stepId)
                .status(JobApplicationStepStatus.CURRENT)
                .build();

        CreateInterviewRequest request = new CreateInterviewRequest(
                UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, null
        );

        when(jobApplicationStepRepository.getOrThrow(stepId, "JobApplicationStep")).thenReturn(step);

        assertThrows(InvalidEntityStateException.class, () -> interviewService.createInterview(stepId, request));

        verify(interviewRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenStepAlreadyHasInterview() {
        UUID stepId = UUID.randomUUID();
        JobApplicationStep step = JobApplicationStep.builder()
                .id(stepId)
                .status(JobApplicationStepStatus.WAITING)
                .build();

        CreateInterviewRequest request = new CreateInterviewRequest(
                UUID.randomUUID(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), null, null, null
        );

        when(jobApplicationStepRepository.getOrThrow(stepId, "JobApplicationStep")).thenReturn(step);
        when(interviewRepository.existsByJobApplicationStepId(stepId)).thenReturn(true);

        assertThrows(InvalidEntityStateException.class, () -> interviewService.createInterview(stepId, request));

        verify(interviewRepository, never()).save(any());
    }

    @Test
    void shouldThrowWhenCreateStartIsNotBeforeEnd() {
        UUID stepId = UUID.randomUUID();
        UUID recruiterId = UUID.randomUUID();
        JobApplicationStep step = JobApplicationStep.builder()
                .id(stepId)
                .status(JobApplicationStepStatus.WAITING)
                .build();

        LocalDateTime start = LocalDateTime.now().plusHours(2);
        LocalDateTime end = start.minusHours(1);
        CreateInterviewRequest request = new CreateInterviewRequest(recruiterId, start, end, null, null, null);

        when(jobApplicationStepRepository.getOrThrow(stepId, "JobApplicationStep")).thenReturn(step);
        when(interviewRepository.existsByJobApplicationStepId(stepId)).thenReturn(false);
        when(userRepository.getOrThrow(recruiterId, "User")).thenReturn(User.builder().id(recruiterId).build());

        assertThrows(InvalidEntityStateException.class, () -> interviewService.createInterview(stepId, request));

        verify(interviewRepository, never()).save(any());
    }

    // ---------- updateInterviewDetails ----------

    @Test
    void shouldThrowWhenUpdatingInterviewNotBelongingToJobApplication() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        UpdateInterviewRequest request = new UpdateInterviewRequest(null, null, null, null, null, null);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.updateInterviewDetails(jobApplicationId, interviewId, request)
        );
    }

    @Test
    void shouldThrowWhenUpdatedStartIsAfterEnd() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime originalStart = LocalDateTime.now();
        LocalDateTime originalEnd = originalStart.plusHours(1);
        Interview interview = scheduledInterview(interviewId, originalStart, originalEnd);

        UpdateInterviewRequest request = new UpdateInterviewRequest(
                null, originalEnd.plusHours(1), null, null, null, null
        );

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.updateInterviewDetails(jobApplicationId, interviewId, request)
        );

        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldPublishEventWhenScheduledStartChanges() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime originalStart = LocalDateTime.now();
        LocalDateTime originalEnd = originalStart.plusHours(1);
        Interview interview = scheduledInterview(interviewId, originalStart, originalEnd);

        LocalDateTime newStart = originalStart.plusMinutes(15);
        UpdateInterviewRequest request = new UpdateInterviewRequest(null, newStart, null, null, null, null);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);

        assertEquals(newStart, interview.getScheduledStart());

        ArgumentCaptor<InterviewRescheduledEvent> captor = ArgumentCaptor.forClass(InterviewRescheduledEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        InterviewRescheduledEvent event = captor.getValue();

        assertEquals(interviewId, event.interviewId());
        assertEquals(jobApplicationId, event.jobApplicationId());
        assertEquals(originalStart, event.oldScheduledStart());
        assertEquals(newStart, event.newScheduledStart());
        assertEquals(originalEnd, event.oldScheduledEnd());
        assertEquals(originalEnd, event.newScheduledEnd());
    }

    @Test
    void shouldPublishEventWhenLocationChanges() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Interview interview = scheduledInterview(interviewId, start, end);

        UpdateInterviewRequest request = new UpdateInterviewRequest(null, null, null, "New room", null, null);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);

        assertEquals("New room", interview.getLocation());
        verify(eventPublisher).publishEvent(any(InterviewRescheduledEvent.class));
    }

    @Test
    void shouldPublishEventWhenMeetingUrlChanges() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Interview interview = scheduledInterview(interviewId, start, end);

        UpdateInterviewRequest request = new UpdateInterviewRequest(
                null, null, null, null, "https://meet.example.com/updated", null
        );

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);

        assertEquals("https://meet.example.com/updated", interview.getMeetingUrl());
        verify(eventPublisher).publishEvent(any(InterviewRescheduledEvent.class));
    }

    @Test
    void shouldNotPublishEventWhenOnlyRecruiterChanges() {
        // NOTE: per current InterviewService logic, the event only fires for
        // scheduledStart/scheduledEnd/location/meetingUrl changes. Recruiter
        // reassignment alone does NOT trigger InterviewRescheduledEvent today,
        // even though the README's tracking-fields list includes "recruiter".
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Interview interview = scheduledInterview(interviewId, start, end);
        UUID newRecruiterId = UUID.randomUUID();
        User newRecruiter = User.builder().id(newRecruiterId).build();

        UpdateInterviewRequest request = new UpdateInterviewRequest(newRecruiterId, null, null, null, null, null);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(userRepository.getOrThrow(newRecruiterId, "User")).thenReturn(newRecruiter);
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);

        assertEquals(newRecruiter, interview.getRecruiter());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldNotPublishEventWhenOnlyStatusChangesToCancelled() {
        // NOTE: same discrepancy as above — a bare status change to CANCELLED
        // (or RESCHEDULED) does not currently trigger the event by itself.
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Interview interview = scheduledInterview(interviewId, start, end);

        UpdateInterviewRequest request = new UpdateInterviewRequest(
                null, null, null, null, null, InterviewStatus.CANCELLED
        );

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);

        assertEquals(InterviewStatus.CANCELLED, interview.getStatus());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    void shouldNotPublishEventWhenNothingRelevantChanges() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        Interview interview = scheduledInterview(interviewId, start, end);

        UpdateInterviewRequest request = new UpdateInterviewRequest(null, null, null, null, null, null);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));
        when(interviewMapper.toResponse(interview)).thenReturn(dummyResponse);

        interviewService.updateInterviewDetails(jobApplicationId, interviewId, request);

        verify(eventPublisher, never()).publishEvent(any());
    }

    // ---------- deleteInterview ----------

    @Test
    void shouldSoftDeleteInterview() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        Interview interview = scheduledInterview(interviewId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));

        interviewService.deleteInterview(jobApplicationId, interviewId);

        assertTrue(interview.isDeleted());
    }

    @Test
    void shouldThrowWhenDeletingAlreadyDeletedInterview() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        Interview interview = scheduledInterview(interviewId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        interview.setDeleted(true);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.deleteInterview(jobApplicationId, interviewId)
        );
    }

    @Test
    void shouldThrowWhenDeletingInterviewNotBelongingToJobApplication() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.deleteInterview(jobApplicationId, interviewId)
        );
    }

    // ---------- restoreInterview ----------

    @Test
    void shouldRestoreDeletedInterview() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        Interview interview = scheduledInterview(interviewId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        interview.setDeleted(true);

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));

        interviewService.restoreInterview(jobApplicationId, interviewId);

        assertFalse(interview.isDeleted());
    }

    @Test
    void shouldThrowWhenRestoringNonDeletedInterview() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();
        Interview interview = scheduledInterview(interviewId, LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.of(interview));

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.restoreInterview(jobApplicationId, interviewId)
        );
    }

    @Test
    void shouldThrowWhenRestoringInterviewNotBelongingToJobApplication() {
        UUID jobApplicationId = UUID.randomUUID();
        UUID interviewId = UUID.randomUUID();

        when(interviewRepository.findByIdAndJobApplicationId(interviewId, jobApplicationId))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidEntityStateException.class,
                () -> interviewService.restoreInterview(jobApplicationId, interviewId)
        );
    }
}
