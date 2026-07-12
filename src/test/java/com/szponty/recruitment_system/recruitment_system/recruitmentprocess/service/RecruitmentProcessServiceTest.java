package com.szponty.recruitment_system.recruitment_system.recruitmentprocess.service;

import com.szponty.recruitment_system.common.exception.InvalidEntityStateException;
import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.recruitmentprocess.DTO.CreateRecruitmentProcessRequest;
import com.szponty.recruitment_system.recruitmentprocess.DTO.RecruitmentProcessResponse;
import com.szponty.recruitment_system.recruitmentprocess.mapper.RecruitmentProcessMapper;
import com.szponty.recruitment_system.recruitmentprocess.model.RecruitmentProcess;
import com.szponty.recruitment_system.recruitmentprocess.repository.RecruitmentProcessRepository;
import com.szponty.recruitment_system.recruitmentprocess.service.RecruitmentProcessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecruitmentProcessServiceTest {

    @Mock
    private RecruitmentProcessRepository recruitmentProcessRepository;

    @Mock
    private RecruitmentProcessMapper processMapper;

    @InjectMocks
    private RecruitmentProcessService service;

    @Test
    void shouldReturnAllRecruitmentProcesses() {
        RecruitmentProcess p1 = RecruitmentProcess.builder().build();
        RecruitmentProcess p2 = RecruitmentProcess.builder().build();
        List<RecruitmentProcess> processes = List.of(p1, p2);

        RecruitmentProcessResponse r1 = mock(RecruitmentProcessResponse.class);
        RecruitmentProcessResponse r2 = mock(RecruitmentProcessResponse.class);

        when(recruitmentProcessRepository.findAll()).thenReturn(processes);
        when(processMapper.toResponse(p1)).thenReturn(r1);
        when(processMapper.toResponse(p2)).thenReturn(r2);

        List<RecruitmentProcessResponse> result = service.getAllRecruitmentProcesses();

        assertEquals(List.of(r1, r2), result);
        verify(recruitmentProcessRepository).findAll();
        verify(processMapper).toResponse(p1);
        verify(processMapper).toResponse(p2);
    }

    @Test
    void shouldReturnRecruitmentProcessById() {
        UUID id = UUID.randomUUID();
        RecruitmentProcess process = RecruitmentProcess.builder().build();
        RecruitmentProcessResponse response = mock(RecruitmentProcessResponse.class);

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.of(process));
        when(processMapper.toResponse(process)).thenReturn(response);

        RecruitmentProcessResponse result = service.getRecruitmentProcessById(id);

        assertSame(response, result);
        verify(recruitmentProcessRepository).findById(id);
        verify(processMapper).toResponse(process);
    }

    @Test
    void shouldThrowNotFoundWhenGettingNonExistentRecruitmentProcess() {
        UUID id = UUID.fromString("00000000-0000-0000-0000-000000000003");

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.getRecruitmentProcessById(id)
        );

        assertTrue(ex.getMessage().contains(id.toString()));
        verify(processMapper, never()).toResponse(any());
    }

    @Test
    void shouldCreateRecruitmentProcess() {
        CreateRecruitmentProcessRequest request = mock(CreateRecruitmentProcessRequest.class);
        RecruitmentProcess processFromRequest = RecruitmentProcess.builder().build();
        RecruitmentProcess savedProcess = RecruitmentProcess.builder().build();
        RecruitmentProcessResponse response = mock(RecruitmentProcessResponse.class);

        when(processMapper.toEntity(request)).thenReturn(processFromRequest);
        when(recruitmentProcessRepository.save(processFromRequest)).thenReturn(savedProcess);
        when(processMapper.toResponse(savedProcess)).thenReturn(response);

        RecruitmentProcessResponse result = service.createRecruitmentProcess(request);

        assertSame(response, result);
        verify(processMapper).toEntity(request);
        verify(recruitmentProcessRepository).save(processFromRequest);
        verify(processMapper).toResponse(savedProcess);
    }

    @Test
    void shouldDeleteRecruitmentProcess() {
        UUID id = UUID.randomUUID();
        RecruitmentProcess process = RecruitmentProcess.builder()
                .deleted(false)
                .build();

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.of(process));

        service.deleteRecruitmentProcess(id);

        assertTrue(process.isDeleted());
        verify(recruitmentProcessRepository).findById(id);
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistentRecruitmentProcess() {
        UUID id = UUID.randomUUID();

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.deleteRecruitmentProcess(id)
        );

        assertTrue(ex.getMessage().contains(id.toString()));
    }

    @Test
    void shouldThrowInvalidEntityStateExceptionWhenDeletingAlreadyDeletedRecruitmentProcess() {
        UUID id = UUID.randomUUID();
        RecruitmentProcess process = RecruitmentProcess.builder()
                .deleted(true)
                .build();

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.of(process));

        InvalidEntityStateException ex = assertThrows(InvalidEntityStateException.class,
                () -> service.deleteRecruitmentProcess(id)
        );

        assertTrue(ex.getMessage().contains(id.toString()));
    }

    @Test
    void shouldRestoreRecruitmentProcess() {
        UUID id = UUID.randomUUID();
        RecruitmentProcess process = RecruitmentProcess.builder()
                .deleted(true)
                .build();

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.of(process));

        service.restoreRecruitmentProcess(id);

        assertFalse(process.isDeleted());
        verify(recruitmentProcessRepository).findById(id);
    }

    @Test
    void shouldThrowNotFoundWhenRestoringNonExistentRecruitmentProcess() {
        UUID id = UUID.randomUUID();

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> service.restoreRecruitmentProcess(id)
        );

        assertTrue(ex.getMessage().contains(id.toString()));
    }

    @Test
    void shouldThrowInvalidEntityStateExceptionWhenRestoringNotDeletedRecruitmentProcess() {
        UUID id = UUID.randomUUID();
        RecruitmentProcess process = RecruitmentProcess.builder()
                .deleted(false)
                .build();

        when(recruitmentProcessRepository.findById(id)).thenReturn(Optional.of(process));

        InvalidEntityStateException ex = assertThrows(InvalidEntityStateException.class,
                () -> service.restoreRecruitmentProcess(id)
        );

        assertTrue(ex.getMessage().contains(id.toString()));
    }
}