package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.ContractType;
import com.szponty.recruitment_system.dictionary.repository.ContractTypeRepository;

public class ContractTypeService extends AbstractDictionaryService<ContractType> {
    public ContractTypeService(
            ContractTypeRepository repository,
            DictionaryMapper dictionaryMapper
    ) {
        super(repository, dictionaryMapper);
    }

    @Override
    protected ContractType createEntity() { return new ContractType(); }
}
