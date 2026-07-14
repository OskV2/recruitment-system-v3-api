package com.szponty.recruitment_system.common.repository;

import com.szponty.recruitment_system.common.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface FindOrThrowRepository<T, ID> extends JpaRepository<T, ID> {

    default T getOrThrow(ID id, String entityName) {
        return findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName, id));
    }
}