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
        return locationRepository.findAllByDeletedFalse()
                .stream()
                .map(dictionaryMapper::toLocationResponse)
                .toList();
    }

    public LocationResponse getLocationById(UUID locationId) {
        Location location = locationRepository.findByIdAndDeletedFalse(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found."));

        return dictionaryMapper.toLocationResponse(location);

    }

    public LocationResponse createLocation(LocationRequest request) {
        Location location = locationRepository.save(dictionaryMapper.toCreateLocationEntity(request));

        return dictionaryMapper.toLocationResponse(location);
    }

    public LocationResponse updateLocation(UUID locationId, LocationRequest request) {
        Location location = locationRepository.findByIdAndDeletedFalse(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid location id"));

        dictionaryMapper.toUpdateLocationEntity(request, location);

        Location savedLocation = locationRepository.save(location);

        return dictionaryMapper.toLocationResponse(savedLocation);
    }

    public void deleteLocation(UUID locationId) {
        Location location = locationRepository.findByIdAndDeletedFalse(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid location id"));

        location.setDeleted(true);
        locationRepository.save(location);
    }

}
