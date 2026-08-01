package com.szponty.recruitment_system.dictionary.repository;

import com.szponty.recruitment_system.dictionary.model.Benefit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
public class BenefitRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:latest");

    @Autowired
    BenefitRepository benefitRepository;

    @Test
    void shouldSaveBenefit() {
        Benefit benefit = Benefit.builder()
                .name("Multisport")
                .description("Sports card")
                .build();

        Benefit saved = benefitRepository.save(benefit);

        assertNotNull(saved.getId());
        assertEquals("Multisport", benefit.getName());
        assertEquals("Sports card", benefit.getDescription());
    }

    @Test
    void shouldFindBenefitById() {
        Benefit saved = benefitRepository.save(
                Benefit.builder()
                        .name("Multisport")
                        .description("Sports card")
                        .build()
        );

        Optional<Benefit> result =
                benefitRepository.findById(saved.getId());

        assertTrue(result.isPresent());
        assertEquals("Multisport", result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenBenefitDoesNotExist() {
        Optional<Benefit> result =
                benefitRepository.findById(UUID.randomUUID());

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldUpdateBenefit() {
        Benefit saved = benefitRepository.save(
                Benefit.builder()
                        .name("Old name")
                        .description("Old description")
                        .build()
        );

        saved.setName("Multisport");
        benefitRepository.save(saved);

        Benefit updated = benefitRepository
                .findById(saved.getId())
                .orElseThrow();

        assertEquals("Multisport", updated.getName());
    }

    @Test
    void shouldNotReturnDeletedBenefits() {
        benefitRepository.save(
                Benefit.builder()
                        .name("Deleted benefit")
                        .deleted(true)
                        .build()
        );

        List<Benefit> result =
                benefitRepository.findAllByDeletedFalse();

        assertTrue(result.isEmpty());
    }
}
