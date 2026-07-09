package com.szponty.recruitment_system.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessVersionRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessVersionResponse;
import com.szponty.recruitment_system.recruitmentprocess.mapper.RecruitmentProcessVersionMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.ProcessStep;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcessVersion;
import com.szponty.recruitment_system.recruitmentprocess.repository.ProcessStepRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessRepository;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessVersionRepository;
import com.szponty.recruitment_system.recruitmentprocess.service.RecruitmentProcessVersionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecruitmentProcessVersionServiceTest {

    @Mock ProcessStepRepository processStepRepository;
    @Mock RecruitmentProcessRepository recruitmentProcessRepository;
    @Mock RecruitmentProcessVersionRepository recruitmentProcessVersionRepository;
    @Mock RecruitmentProcessVersionMapper mapper;

    @InjectMocks
    RecruitmentProcessVersionService service;

    @Test
    void shouldReturnVersionById() {
        UUID id = UUID.randomUUID();
        RecruitmentProcessVersion entity = RecruitmentProcessVersion.builder().build();
        RecruitmentProcessVersionResponse response = mock(RecruitmentProcessVersionResponse.class);

        when(recruitmentProcessVersionRepository.findById(id))
                .thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity))
                .thenReturn(response);

        RecruitmentProcessVersionResponse result =
                service.getRecruitmentProcessVersionById(id);

        assertSame(response, result);
        verify(recruitmentProcessVersionRepository).findById(id);
        verify(mapper).toResponse(entity);
    }

    @Test
    void shouldReturnAllVersionsByRecruitmentProcessId() {
        UUID id = UUID.randomUUID();

        RecruitmentProcessVersion v1 = RecruitmentProcessVersion.builder().build();
        RecruitmentProcessVersion v2 = RecruitmentProcessVersion.builder().build();

        List<RecruitmentProcessVersion> entities = List.of(v1, v2);

        RecruitmentProcess process = RecruitmentProcess.builder().build();
        when(recruitmentProcessRepository.findById(id))
                .thenReturn(Optional.of(process));

        when(recruitmentProcessVersionRepository.findByRecruitmentProcess(process))
                .thenReturn(entities);

        RecruitmentProcessVersionResponse r1 = mock(RecruitmentProcessVersionResponse.class);
        RecruitmentProcessVersionResponse r2 = mock(RecruitmentProcessVersionResponse.class);

        when(mapper.toResponse(v1)).thenReturn(r1);
        when(mapper.toResponse(v2)).thenReturn(r2);

        List<RecruitmentProcessVersionResponse> result =
                service.getAllRecruitmentProcVersionsByRecruitmentProcessId(id);

        assertEquals(List.of(r1, r2), result);

        verify(recruitmentProcessRepository).findById(id);
        verify(recruitmentProcessVersionRepository)
                .findByRecruitmentProcess(process);
        verify(mapper).toResponse(v1);
        verify(mapper).toResponse(v2);
    }

    @Test
    void shouldCreateVersion() {
        UUID processId = UUID.randomUUID();
        UUID stepId = UUID.randomUUID();

        RecruitmentProcess process = RecruitmentProcess.builder()
                .id(processId)
                .build();

        ProcessStep step = ProcessStep.builder().id(stepId).build();

        CreateRecruitmentProcessVersionRequest request =
                new CreateRecruitmentProcessVersionRequest(List.of(stepId));

        RecruitmentProcessVersionResponse response =
                mock(RecruitmentProcessVersionResponse.class);

        when(recruitmentProcessRepository.findById(processId))
                .thenReturn(Optional.of(process));
        when(processStepRepository.findAllById(List.of(stepId)))
                .thenReturn(List.of(step));
        when(recruitmentProcessVersionRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toResponse(any()))
                .thenReturn(response);

        RecruitmentProcessVersionResponse result =
                service.createRecruitmentProcessVersion(processId, request);

        assertSame(response, result);

        ArgumentCaptor<RecruitmentProcessVersion> captor =
                ArgumentCaptor.forClass(RecruitmentProcessVersion.class);
        verify(recruitmentProcessVersionRepository).save(captor.capture());

        RecruitmentProcessVersion saved = captor.getValue();
        assertSame(process, saved.getRecruitmentProcess());
        assertEquals(List.of(step), saved.getSteps());
    }

    @Test
    void shouldInactivateVersion() {
        UUID id = UUID.randomUUID();
        UUID processId = UUID.randomUUID();

        RecruitmentProcess process = RecruitmentProcess.builder()
                        .id(processId)
                        .build();

        RecruitmentProcessVersion entity = RecruitmentProcessVersion.builder()
                        .recruitmentProcess(process)
                        .active(true)
                        .build();

        when(recruitmentProcessVersionRepository.findById(id))
                .thenReturn(Optional.of(entity));

        when(recruitmentProcessVersionRepository
                .countByRecruitmentProcessIdAndActiveTrue(processId))
                .thenReturn(2L);

        service.inactivateRecruitmentProcessVersion(id);

        assertFalse(entity.isActive());
    }

    @Test
    void shouldActivateVersion() {
        UUID id = UUID.randomUUID();

        RecruitmentProcessVersion entity = RecruitmentProcessVersion.builder()
                .active(false)
                .build();

        when(recruitmentProcessVersionRepository.findById(id))
                .thenReturn(Optional.of(entity));

        service.activateRecruitmentProcessVersion(id);

        assertTrue(entity.isActive());
    }
}
