package com.szponty.recruitment_system.joboffer.model;

import com.szponty.recruitment_system.dictionary.model.Benefit;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
    name = "job_offer_benefit",
    uniqueConstraints = @UniqueConstraint(columnNames = {"job_offer_id", "benefit_id"})
)
public class JobOfferBenefit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;

    @ManyToOne
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;
}
