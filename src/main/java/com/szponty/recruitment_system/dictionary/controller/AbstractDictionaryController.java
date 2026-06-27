package com.szponty.recruitment_system.dictionary.controller;

import com.szponty.recruitment_system.dictionary.dto.DictionaryItemRequest;
import com.szponty.recruitment_system.dictionary.dto.DictionaryItemResponse;
import com.szponty.recruitment_system.dictionary.model.DictionaryEntity;
import com.szponty.recruitment_system.dictionary.service.AbstractDictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class AbstractDictionaryController<T extends DictionaryEntity> {

    private final AbstractDictionaryService<T> service;

    @GetMapping
    public ResponseEntity<List<DictionaryItemResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DictionaryItemResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<DictionaryItemResponse> create(
            @RequestBody DictionaryItemRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DictionaryItemResponse> update(
            @PathVariable UUID id,
            @RequestBody DictionaryItemRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}