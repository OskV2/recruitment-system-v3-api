package com.szponty.recruitment_system.joboffer.service;

import com.szponty.recruitment_system.joboffer.dto.PublicJobOfferDetailsResponse;
import com.szponty.recruitment_system.joboffer.dto.PublicJobOfferShortResponse;
import com.szponty.recruitment_system.joboffer.mapper.JobOfferMapper;
import com.szponty.recruitment_system.joboffer.model.JobOffer;
import com.szponty.recruitment_system.joboffer.model.JobOfferStatus;
import com.szponty.recruitment_system.joboffer.repository.JobOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicJobOfferService {

    private final JobOfferRepository jobOfferRepository;
    private final JobOfferMapper jobOfferMapper;

    public List<PublicJobOfferShortResponse> getAll() {
        LocalDateTime now = LocalDateTime.now();

        return jobOfferRepository.findPublicOffers(JobOfferStatus.ACTIVE, now)
                .stream()
                .map(jobOfferMapper::toPublicShortResponse)
                .toList();
    }

    public PublicJobOfferDetailsResponse get(UUID id) {
        LocalDateTime now = LocalDateTime.now();

        JobOffer jobOffer = jobOfferRepository
                .findPublicOfferById(id, JobOfferStatus.ACTIVE, now)
                .orElseThrow(() -> new IllegalArgumentException("Job offer not found"));

        return jobOfferMapper.toPublicDetailsResponse(jobOffer);
    }
}
