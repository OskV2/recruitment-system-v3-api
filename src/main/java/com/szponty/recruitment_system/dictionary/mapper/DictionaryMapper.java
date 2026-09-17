package com.szponty.recruitment_system.dictionary.mapper;

import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.dto.LocationRequest;
import com.szponty.recruitment_system.dictionary.dto.LocationResponse;
import com.szponty.recruitment_system.dictionary.model.DictionaryEntity;
import com.szponty.recruitment_system.dictionary.model.Location;
import org.springframework.stereotype.Component;

@Component
public class DictionaryMapper {

    public DictionaryItemResponse toDictionaryItemResponse(DictionaryEntity entity) {
        return new DictionaryItemResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }

    public LocationResponse toLocationResponse(Location location) {
        return new LocationResponse(
                location.getId(),
                location.getCity(),
                location.getCountry(),
                location.getDescription(),
                location.getCreatedAt()
        );
    }

    public Location toCreateLocationEntity(LocationRequest request) {
        return Location.builder()
                .city(request.city())
                .country(request.country())
                .description(request.description())
                .build();
    }

    public void toUpdateLocationEntity(LocationRequest request, Location location) {
        if (request.city() != null) { location.setCity(request.city()); }

        if (request.country() != null) { location.setCountry(request.country()); }

        if (request.description() != null) { location.setDescription(request.description()); }
    }
}
