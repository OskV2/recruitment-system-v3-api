package com.szponty.recruitment_system.joboffer.model;

import com.szponty.recruitment_system.dictionary.model.Benefit;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "job_offer_benefit")
public class JobOfferBenefit {

    @EmbeddedId
    private JobOfferBenefitId id;

    @ManyToOne
    @MapsId("jobOfferId")
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;

    @ManyToOne
    @MapsId("benefitId")
    @JoinColumn(name = "benefit_id")
    private Benefit benefit;
}
