package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.dto.DictionaryItemRequest;
import com.szponty.recruitment_system.dictionary.dto.LocationRequest;
import com.szponty.recruitment_system.dictionary.dto.LocationResponse;
import com.szponty.recruitment_system.dictionary.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/dictionary/location")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getAllLocations() {
        return ResponseEntity.ok(locationService.getAllLocations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> getLocationById(@PathVariable UUID id) {
        return ResponseEntity.ok(locationService.getLocationById(id));
    }

    @PostMapping
    public ResponseEntity<LocationResponse> createLocation(@RequestBody LocationRequest request) {
        return ResponseEntity.ok(locationService.createLocation(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LocationResponse> updateLocation(@PathVariable UUID id, @RequestBody LocationRequest request) {
        return ResponseEntity.ok(locationService.updateLocation(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable UUID id) {
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
