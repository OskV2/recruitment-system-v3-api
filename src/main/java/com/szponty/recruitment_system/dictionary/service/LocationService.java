package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.dictionary.dto.LocationRequest;
import com.szponty.recruitment_system.dictionary.dto.LocationResponse;
import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.Location;
import com.szponty.recruitment_system.dictionary.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final DictionaryMapper dictionaryMapper;

    public List<LocationResponse> getAllLocations() {
        return locationRepository.findAll()
                .stream()
                .map(dictionaryMapper::toLocationResponse)
                .toList();
    }

    public LocationResponse getLocationById(UUID locationId) {
        Location location = locationRepository.findByIdAndDeletedFalse()
                .orElseThrow(() -> new IllegalArgumentException("Location not found."));

        return dictionaryMapper.toLocationResponse(location);

    }

    public LocationResponse createLocation(LocationRequest request) {

    }

    public LocationResponse updateLocation(UUID locationId, LocationRequest request) {

    }

    public void deleteLocation() {}

}
