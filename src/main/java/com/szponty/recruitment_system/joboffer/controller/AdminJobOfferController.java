package com.szponty.recruitment_system.joboffer.controller;

import com.szponty.recruitment_system.joboffer.dto.AdminJobOfferDetailsResponse;
import com.szponty.recruitment_system.joboffer.dto.AdminJobOfferShortResponse;
import com.szponty.recruitment_system.joboffer.dto.ChangeJobOfferStatusRequest;
import com.szponty.recruitment_system.joboffer.dto.CreateJobOfferRequest;
import com.szponty.recruitment_system.joboffer.service.AdminJobOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/job-offer")
public class AdminJobOfferController {

    private final AdminJobOfferService adminJobOfferService;

    @GetMapping
    public ResponseEntity<List<AdminJobOfferShortResponse>> getAll() {
        return ResponseEntity.ok(adminJobOfferService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminJobOfferDetailsResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(adminJobOfferService.get(id));
    }

    @PostMapping
    public ResponseEntity<AdminJobOfferDetailsResponse> create(
            @Valid @RequestBody CreateJobOfferRequest request
    ) {
        AdminJobOfferDetailsResponse response = adminJobOfferService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdminJobOfferDetailsResponse> update(
            @PathVariable UUID id,
            @RequestBody CreateJobOfferRequest request
    ) {
        return ResponseEntity.ok(adminJobOfferService.update(id, request));
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<AdminJobOfferDetailsResponse> changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody ChangeJobOfferStatusRequest request
    ) {
        return ResponseEntity.ok(adminJobOfferService.changeStatus(id, request));
    }
}