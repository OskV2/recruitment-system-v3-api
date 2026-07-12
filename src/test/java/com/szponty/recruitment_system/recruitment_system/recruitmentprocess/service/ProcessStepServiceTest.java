package com.szponty.recruitment_system.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidProcessStepStateException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.ProcessStepResponse;
import com.szponty.recruitment_system.recruitmentprocess.DTO.UpdateProcessStepRequest;
import com.szponty.recruitment_system.recruitmentprocess.mapper.ProcessStepMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import com.szponty.recruitment_system.recruitmentprocess.service.ProcessStepService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessStepServiceTest {

    @Mock
    ProcessStepRepository processStepRepository;
    @Mock
    RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;
    @Mock
    ProcessStepMapper processStepMapper;

    @InjectMocks
    ProcessStepService service;

    @Test
    void shouldReturnAllNotDeletedSteps() {
        ProcessStep p1 = ProcessStep.builder().build();
        ProcessStep p2 = ProcessStep.builder().build();

        List<ProcessStep> processStepList = List.of(p1, p2);

        when(processStepRepository.findByDeleted(false))
                .thenReturn(processStepList);

        ProcessStepResponse r1 = new ProcessStepResponse(
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                "TestProcessStep1",
                "Test process step",
                false,
                false,
                LocalDateTime.of(2025, 1, 1, 12, 0)
        );

        ProcessStepResponse r2 = new ProcessStepResponse(
                UUID.fromString("00000000-0000-0000-0000-000000000003"),
                UUID.fromString("00000000-0000-0000-0000-000000000004"),
                "TestProcessStep2",
                "Test process step",
                true,
                false,
                LocalDateTime.of(2025, 2, 2, 23, 55)
        );

        when(processStepMapper.toResponse(p1)).thenReturn(r1);
        when(processStepMapper.toResponse(p2)).thenReturn(r2);

        List<ProcessStepResponse> result = service.getAllSteps(false);

        assertEquals(List.of(r1, r2), result);

        verify(processStepRepository).findByDeleted(false);
        verify(processStepMapper).toResponse(p1);
        verify(processStepMapper).toResponse(p2);
    }

    @Test
    void shouldReturnProcessStepById() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");
        ProcessStep step = ProcessStep.builder().build();

        ProcessStepResponse r = new ProcessStepResponse(
                id,
                UUID.fromString("00000000-0000-0000-0000-000000000004"),
                "TestProcessStep",
                "Test process step",
                true,
                false,
                LocalDateTime.of(2025, 2, 2, 23, 55)
        );

        when(processStepRepository.findById(id))
                .thenReturn(Optional.of(step));

        when(processStepMapper.toResponse(step))
                .thenReturn(r);

        ProcessStepResponse result = service.getProcessStepById(id);

        assertEquals(result, r);

        verify(processStepRepository).findById(id);
        verify(processStepMapper).toResponse(step);
    }

    @Test
    void shouldThrowNotFoundWhenGettingNonExistentProcessStep() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.getProcessStepById(id)
        );

        assertTrue(ex.getMessage().contains(id.toString()));
        verify(processStepMapper, never()).toResponse(any());
    }

    @Test
    void shouldCreateProcessStep() {
        UUID versionId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        CreateProcessStepRequest request = new CreateProcessStepRequest(
                versionId,
                "Interview with future manager",
                "",
                true,
                true
        );

        RecruitmentProcessVersion version = mock(RecruitmentProcessVersion.class);
        ProcessStep step = ProcessStep.builder().build();

        ProcessStepResponse response = new ProcessStepResponse(
                UUID.fromString("00000000-0000-0000-0000-000000000002"),
                versionId,
                "Interview with future manager",
                "",
                true,
                true,
                LocalDateTime.of(2025, 1, 1, 12, 0)
        );

        when(recruitmentProcessVersionRepository.findById(versionId))
                .thenReturn(Optional.of(version));

        when(processStepMapper.toEntity(request, version))
                .thenReturn(step);

        when(processStepRepository.save(step))
                .thenReturn(step);

        when(processStepMapper.toResponse(step))
                .thenReturn(response);

        ProcessStepResponse result = service.createProcessStep(request);

        assertEquals(response, result);

        verify(recruitmentProcessVersionRepository).findById(versionId);
        verify(processStepMapper).toEntity(request, version);
        verify(processStepRepository).save(step);
        verify(processStepMapper).toResponse(step);
    }

    @Test
    void shouldThrowNotFoundWhenCreatingProcessStepForNonExistentVersion() {
        UUID versionId = UUID.randomUUID();

        CreateProcessStepRequest request = new CreateProcessStepRequest(
                versionId, "Step", "", false, false);

        when(recruitmentProcessVersionRepository.findById(versionId))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createProcessStep(request));

        verify(processStepRepository, never()).save(any());
    }


    @Test
    void shouldThrowNotFoundWhenUpdatingNonExistentProcessStep() {
        UUID id = UUID.randomUUID();

        when(processStepRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.updateProcessStep(
                        id,
                        new UpdateProcessStepRequest(null, null, null, null, null)
                ));
    }

    @Test
    void shouldUpdateProcessStep() {
        UUID processStepId = UUID.fromString("00000000-0000-0000-0000-000000000003");

        UpdateProcessStepRequest request = new UpdateProcessStepRequest(
            null,
            null,
            "New description",
            null,
            null
        );

        ProcessStep processStep = ProcessStep.builder()
                .id(processStepId)
                .name("TestProcessStep")
                .description("Test Description")
                .requiresInterview(false)
                .requiresDepartmentApproval(false)
                .build();

        when(processStepRepository.findById(processStepId))
                .thenReturn(Optional.of(processStep));

        ProcessStepResponse response = new ProcessStepResponse(
                processStepId,
                UUID.fromString("00000000-0000-0000-0000-000000000001"),
                "TestProcessStep",
                "New description",
                false,
                false,
                LocalDateTime.of(2025, 1, 1, 12, 0)
        );

        when(processStepMapper.toResponse(processStep))
                .thenReturn(response);

        ProcessStepResponse result = service.updateProcessStep(processStepId, request);

        assertEquals("New description", processStep.getDescription());
        assertEquals("TestProcessStep", processStep.getName());
        assertEquals(response, result);

        verify(processStepRepository).findById(processStepId);
        verify(processStepMapper).toResponse(processStep);
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistentProcessStep() {
        UUID id = UUID.randomUUID();

        when(processStepRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.deleteProcessStep(id));
    }

    @Test
    void shouldThrowInvalidProcessStepStateExceptionWhenDeletingStep() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");

        ProcessStep step = ProcessStep.builder()
                .id(id)
                .deleted(true)
                .build();

        when(processStepRepository.findById(id))
                .thenReturn(Optional.of(step));

        assertThrows(
                InvalidProcessStepStateException.class,
                () -> service.deleteProcessStep(id)
        );

        verify(processStepRepository).findById(id);
    }


    @Test
    void shouldDeleteProcessStep() {
        UUID processStepId = UUID.fromString("00000000-0000-0000-0000-000000000003");

        ProcessStep processStep = ProcessStep.builder()
                .id(processStepId)
                .name("TestProcessStep")
                .description("Test")
                .requiresDepartmentApproval(false)
                .requiresInterview(false)
                .deleted(false)
                .build();

        when(processStepRepository.findById(processStepId))
                .thenReturn(Optional.of(processStep));

        service.deleteProcessStep(processStepId);

        assertTrue(processStep.isDeleted());

        verify(processStepRepository).findById(processStepId);
    }

    @Test
    void shouldThrowNotFoundWhenRestoringNonExistentProcessStep() {
        UUID id = UUID.randomUUID();

        when(processStepRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.restoreProcessStep(id));
    }

    @Test
    void shouldThrowInvalidProcessStepStateExceptionWhenRestoringStep() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");

        ProcessStep step = ProcessStep.builder()
                .id(id)
                .deleted(false)
                .build();

        when(processStepRepository.findById(id))
                .thenReturn(Optional.of(step));

        assertThrows(
                InvalidProcessStepStateException.class,
                () -> service.restoreProcessStep(id)
        );

        verify(processStepRepository).findById(id);
    }

    @Test
    void shouldRestoreProcessStep() {
        UUID processStepId = UUID.fromString("00000000-0000-0000-0000-000000000003");

        ProcessStep processStep = ProcessStep.builder()
                .id(processStepId)
                .name("TestProcessStep")
                .description("Test")
                .requiresDepartmentApproval(false)
                .requiresInterview(false)
                .deleted(true)
                .build();

        when(processStepRepository.findById(processStepId))
                .thenReturn(Optional.of(processStep));

        service.restoreProcessStep(processStepId);

        assertFalse(processStep.isDeleted());

        verify(processStepRepository).findById(processStepId);
    }
}
