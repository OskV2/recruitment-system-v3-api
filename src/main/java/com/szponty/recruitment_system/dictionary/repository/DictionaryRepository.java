package com.szponty.recruitment_system.dictionary.repository;

import com.szponty.recruitment_system.dictionary.model.DictionaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface DictionaryRepository<T extends DictionaryEntity>
        extends JpaRepository<T, UUID> {

    List<T> findAllByDeletedFalse();

    Optional<T> findByIdDeletedFalse(UUID id);

    boolean existsByNameIgnoreCaseAndDeletedFalse(String name);
}
