package com.szponty.recruitment_system.joboffer.controller;

import com.szponty.recruitment_system.joboffer.dto.PublicJobOfferDetailsResponse;
import com.szponty.recruitment_system.joboffer.dto.PublicJobOfferShortResponse;
import com.szponty.recruitment_system.joboffer.service.PublicJobOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/job-offer")
public class PublicJobOfferController {

    private final PublicJobOfferService publicJobOfferService;

    @GetMapping
    public ResponseEntity<List<PublicJobOfferShortResponse>> getAll() {
        return ResponseEntity.ok(publicJobOfferService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicJobOfferDetailsResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(publicJobOfferService.get(id));
    }
}