package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.common.exception.ResourceNotFoundException;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemRequest;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.Benefit;
import com.szponty.recruitment_system.dictionary.repository.BenefitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BenefitServiceTest {

    @Mock
    private BenefitRepository benefitRepository;

    @Mock
    private DictionaryMapper dictionaryMapper;

    @InjectMocks
    private BenefitService benefitService;

    @Test
    void shouldCreateBenefit() {
        DictionaryItemRequest request = new DictionaryItemRequest(
                "Private healthcare",
                "Luxmed package"
        );

        Benefit savedBenefit = Benefit.builder()
                .id(UUID.randomUUID())
                .name("Private healthcare")
                .description("Luxmed package")
                .build();

        DictionaryItemResponse response = new DictionaryItemResponse(
                savedBenefit.getId(),
                "Private healthcare",
                "Luxmed package",
                LocalDateTime.now()
        );

        when(benefitRepository.save(any(Benefit.class)))
                .thenReturn(savedBenefit);

        when(dictionaryMapper.toDictionaryItemResponse(savedBenefit))
                .thenReturn(response);

        DictionaryItemResponse result = benefitService.create(request);

        assertEquals("Private healthcare", result.name());
        assertEquals("Luxmed package", result.description());

        verify(benefitRepository).save(any(Benefit.class));
        verify(dictionaryMapper).toDictionaryItemResponse(savedBenefit);
    }

    @Test
    void shouldUpdateBenefit() {
        UUID benefitId = UUID.randomUUID();

        DictionaryItemRequest request = new DictionaryItemRequest(
                "Multisport",
                "Sports card"
        );

        Benefit benefit = Benefit.builder()
                .id(benefitId)
                .name("Old name")
                .description("Old description")
                .build();

        DictionaryItemResponse response = new DictionaryItemResponse(
                benefitId,
                "Multisport",
                "Sports card",
                LocalDateTime.now()
        );

        when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.of(benefit));

        when(dictionaryMapper.toDictionaryItemResponse(benefit))
                .thenReturn(response);

        DictionaryItemResponse result = benefitService.update(benefitId, request);

        assertEquals("Multisport", benefit.getName());
        assertEquals("Sports card", benefit.getDescription());

        assertEquals("Multisport", result.name());
        assertEquals("Sports card", result.description());

        verify(benefitRepository).findById(benefitId);
        verify(dictionaryMapper).toDictionaryItemResponse(benefit);
    }

    @Test
    void shouldThrowWhenBenefitDoesNotExistOnUpdate() {
        UUID benefitId = UUID.randomUUID();

        DictionaryItemRequest request = new DictionaryItemRequest(
                "Multisport",
                "Sports card"
        );

        when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> benefitService.update(benefitId, request)
        );

        verify(benefitRepository).findById(benefitId);
        verify(dictionaryMapper, never()).toDictionaryItemResponse(any());
    }

    @Test
    void shouldSoftDeleteBenefit() {
        UUID benefitId = UUID.randomUUID();

        Benefit benefit = Benefit.builder()
                .id(benefitId)
                .name("Multisport")
                .description("Sports card")
                .deleted(false)
                .build();

        when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.of(benefit));

        benefitService.delete(benefitId);

        assertTrue(benefit.isDeleted());

        verify(benefitRepository).findById(benefitId);
    }
}