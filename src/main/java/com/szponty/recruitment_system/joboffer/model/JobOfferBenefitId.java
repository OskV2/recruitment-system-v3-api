package com.szponty.recruitment_system.joboffer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class JobOfferBenefitId implements Serializable {

    @Column(name = "job_offer_id")
    private UUID jobOfferId;

    @Column(name = "benefit_id")
    private UUID benefitId;
}
