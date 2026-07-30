package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemRequest;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.Benefit;
import com.szponty.recruitment_system.dictionary.repository.BenefitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BenefitServiceTest {

    @Mock
    private BenefitRepository benefitRepository;

    private DictionaryMapper dictionaryMapper;
    private BenefitService benefitService;

    private UUID benefitId;
    private Benefit benefit;
    private DictionaryItemRequest createRequest;
    private DictionaryItemRequest updateRequest;

    @BeforeEach
    void setUp() {
        dictionaryMapper = new DictionaryMapper();
        benefitService = new BenefitService(benefitRepository, dictionaryMapper);

        benefitId = UUID.randomUUID();

        benefit = Benefit.builder()
                .id(benefitId)
                .name("Benefit name")
                .description("Benefit description")
                .deleted(false)
                .build();

        createRequest = new DictionaryItemRequest(
                "Benefit name",
                "Benefit description"
        );

        updateRequest = new DictionaryItemRequest(
                "Multisport",
                "Sports card"
        );
    }

    @Test
    void shouldCreateBenefit() {
        mockSave(benefit);
        
        DictionaryItemResponse result = benefitService.create(createRequest);

        assertEquals(benefit.getId(), result.id());
        assertEquals("Benefit name", result.name());
        assertEquals("Benefit description", result.description());
    }

    @Test
    void shouldUpdateBenefit() {
        mockBenefitExists();
        mockSave(benefit);

        assertEquals("Benefit name", benefit.getName());
        assertEquals("Benefit description", benefit.getDescription());

        DictionaryItemResponse result = benefitService.update(benefitId, updateRequest);

        assertEquals("Multisport", result.name());
        assertEquals("Sports card", result.description());

        verify(benefitRepository).findById(benefitId);
        verify(benefitRepository).save(benefit);
    }

    @Test
    void shouldSoftDeleteBenefit() {
        mockBenefitExists();

        benefitService.delete(benefitId);
        assertTrue(benefit.isDeleted());
        verify(benefitRepository).findById(benefitId);
    }

    @Test
    void shouldThrowWhenBenefitDoesNotExistOnUpdate() {
        mockBenefitDoesNotExist();

        assertThrows(
                NotFoundException.class,
                () -> benefitService.update(benefitId, updateRequest)
        );
    }

    private void mockBenefitExists() {
        when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.of(benefit));
    }

    private void mockBenefitDoesNotExist() {
        when(benefitRepository.findById(benefitId))
                .thenReturn(Optional.empty());
    }

    private void mockSave(Benefit savedBenefit) {
        when(benefitRepository.save(any(Benefit.class)))
                .thenReturn(savedBenefit);
    }
}