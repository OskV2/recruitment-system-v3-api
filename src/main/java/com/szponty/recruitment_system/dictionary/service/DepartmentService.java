package com.szponty.recruitment_system.dictionary.service;

import com.szponty.recruitment_system.dictionary.mapper.DictionaryMapper;
import com.szponty.recruitment_system.dictionary.model.Department;
import com.szponty.recruitment_system.dictionary.repository.DepartmentRepository;

import org.springframework.stereotype.Service;

@Service
public class DepartmentService extends AbstractDictionaryService<Department> {
    public DepartmentService(
            DepartmentRepository repository,
            DictionaryMapper dictionaryMapper
    ) {
        super(repository, dictionaryMapper);
    }

    @Override
    protected Department createEntity() {
        return new Department();
    }
}
