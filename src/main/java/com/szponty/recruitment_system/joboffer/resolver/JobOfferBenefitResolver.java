package com.szponty.recruitment_system.joboffer.resolver;

import com.szponty.recruitment_system.dictionary.model.Benefit;
import com.szponty.recruitment_system.dictionary.repository.BenefitRepository;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefit;
import com.szponty.recruitment_system.joboffer.model.JobOfferBenefitId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JobOfferBenefitResolver {

    private final BenefitRepository benefitRepository;

    public Set<JobOfferBenefit> createBenefits(JobOffer jobOffer, Set<UUID> benefitIds) {
        if (benefitIds == null || benefitIds.isEmpty()) {
            return Set.of();
        }

        return benefitIds.stream()
                .map(benefitId -> {
                    Benefit benefit = benefitRepository.getOrThrow(benefitId, "Benefit");

                    return JobOfferBenefit.builder()
                            .jobOffer(jobOffer)
                            .benefit(benefit)
                            .build();
                })
                .collect(Collectors.toSet());
    }

    public void updateBenefits(JobOffer jobOffer, Set<UUID> requestedBenefitIds) {
        jobOffer.getBenefits().removeIf(jobOfferBenefit ->
                !requestedBenefitIds.contains(jobOfferBenefit.getBenefit().getId())
        );

        Set<UUID> currentBenefitIds = jobOffer.getBenefits()
                .stream()
                .map(jobOfferBenefit -> jobOfferBenefit.getBenefit().getId())
                .collect(Collectors.toSet());

        Set<UUID> benefitIdsToAdd = requestedBenefitIds
                .stream()
                .filter(benefitId -> !currentBenefitIds.contains(benefitId))
                .collect(Collectors.toSet());

        List<Benefit> benefitsToAdd = benefitRepository.findAllById(benefitIdsToAdd);

        if (benefitsToAdd.size() != benefitIdsToAdd.size()) {
            throw new IllegalArgumentException("One or more benefits were not found");
        }

        benefitsToAdd.forEach(benefit -> {
            JobOfferBenefit jobOfferBenefit = JobOfferBenefit.builder()
                    .id(new JobOfferBenefitId(jobOffer.getId(), benefit.getId()))
                    .jobOffer(jobOffer)
                    .benefit(benefit)
                    .build();

            jobOffer.getBenefits().add(jobOfferBenefit);
        });
    }
}