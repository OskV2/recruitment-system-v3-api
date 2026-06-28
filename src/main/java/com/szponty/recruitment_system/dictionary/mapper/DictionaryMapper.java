package com.szponty.recruitment_system.dictionary.mapper;

import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.dto.LocationResponse;
import com.szponty.recruitment_system.dictionary.model.DictionaryEntity;
import com.szponty.recruitment_system.dictionary.model.Location;
import jakarta.persistence.metamodel.Type;
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
}
