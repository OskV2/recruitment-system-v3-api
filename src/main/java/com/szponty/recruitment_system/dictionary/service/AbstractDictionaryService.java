package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.common.exception.NotFoundException;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemRequest;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.DictionaryEntity;
import com.szponty.recruitment_system.dictionary.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractDictionaryService<T extends DictionaryEntity> {

    private final DictionaryRepository<T> repository;
    private final DictionaryMapper dictionaryMapper;

    public List<DictionaryItemResponse> getAll() {
        return repository.findAllByDeletedFalse()
                .stream()
                .map(dictionaryMapper::toDictionaryItemResponse)
                .toList();
    }

    public DictionaryItemResponse getById(UUID id) {
        T entity = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Dictionary item not found"));

        return dictionaryMapper.toDictionaryItemResponse(entity);
    }

    public DictionaryItemResponse create(DictionaryItemRequest request) {
        if (repository.existsByNameIgnoreCaseAndDeletedFalse(request.name())) {
            throw new IllegalArgumentException("Dictionary item already exists");
        }

        T entity = createEntity();

        entity.setName(request.name());
        entity.setDescription(request.description());

        return dictionaryMapper.toDictionaryItemResponse(repository.save(entity));
    }

    public DictionaryItemResponse update(UUID id, DictionaryItemRequest request) {
        T entity = repository.findById(id)
                .filter(item -> !item.isDeleted())
                .orElseThrow(() -> new NotFoundException("Dictionary item not found"));

        entity.setName(request.name());
        entity.setDescription(request.description());

        return dictionaryMapper.toDictionaryItemResponse(repository.save(entity));
    }

    public void delete(UUID id) {
        T entity = repository.findById(id)
                .filter(item -> !item.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Dictionary item not found"));

        entity.setDeleted(true);
        repository.save(entity);
    }

    protected abstract T createEntity();
}