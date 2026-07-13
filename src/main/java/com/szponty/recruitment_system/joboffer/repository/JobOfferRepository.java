package com.szponty.recruitment_system.joboffer.repository;

import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobOfferRepository extends JpaRepository<JobOffer, UUID> {
    List<JobOffer> findAllByStatusActive();

    @Query("""
        SELECT jobOffer
        FROM JobOffer jobOffer
          WHERE jobOffer.offerStatus = :status
          AND jobOffer.validFrom <= :now
          AND jobOffer.validTo >= :now
    """)
    List<JobOffer> findPublicOffers(
            @Param("status") JobOfferStatus status,
            @Param("now") LocalDateTime now
    );

    @Query("""
        SELECT jobOffer
        FROM JobOffer jobOffer
        WHERE jobOffer.id = :id
          AND jobOffer.offerStatus = :status
          AND jobOffer.validFrom <= :now
          AND jobOffer.validTo >= :now
    """)
    Optional<JobOffer> findPublicOfferById(
            @Param("id") UUID id,
            @Param("status") JobOfferStatus status,
            @Param("now") LocalDateTime now
    );
}
