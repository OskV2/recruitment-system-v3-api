package com.szponty.recruitment_system.dictionary.mapper;

import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.model.DictionaryEntity;
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
}
