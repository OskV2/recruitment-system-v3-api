package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.FullTimeEquivalent;
import com.szponty.recruitment_system.dictionary.model.WorkModel;
import com.szponty.recruitment_system.dictionary.repository.FullTimeEquivalentRepository;
import com.szponty.recruitment_system.dictionary.repository.WorkModelRepository;

public class WorkModelService extends AbstractDictionaryService<WorkModel> {
    public WorkModelService (
            WorkModelRepository repository,
            DictionaryMapper dictionaryMapper
    ) {
        super(repository, dictionaryMapper);
    }

    @Override
    protected WorkModel createEntity() { return new WorkModel(); }
}
