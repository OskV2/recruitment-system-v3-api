package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.Benefit;
import com.szponty.recruitment_system.dictionary.repository.BenefitRepository;

import org.springframework.stereotype.Service;

@Service
public class BenefitService extends AbstractDictionaryService<Benefit> {
    public BenefitService(
            BenefitRepository repository,
            DictionaryMapper dictionaryMapper
    ) {
        super(repository, dictionaryMapper);
    }

    @Override
    protected Benefit createEntity() {
        return new Benefit();
    }
}
