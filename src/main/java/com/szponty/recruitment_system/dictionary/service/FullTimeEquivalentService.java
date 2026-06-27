package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.FullTimeEquivalent;
import com.szponty.recruitment_system.dictionary.repository.FullTimeEquivalentRepository;

public class FullTimeEquivalentService extends AbstractDictionaryService<FullTimeEquivalent> {
    public FullTimeEquivalentService(
            FullTimeEquivalentRepository repository,
            DictionaryMapper dictionaryMapper
    ) {
        super(repository, dictionaryMapper);
    }

    @Override
    protected FullTimeEquivalent createEntity() { return new FullTimeEquivalent(); }
}
