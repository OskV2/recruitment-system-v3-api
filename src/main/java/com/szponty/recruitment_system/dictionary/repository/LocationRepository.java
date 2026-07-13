package com.szponty.recruitment_system.dictionary.repository;

import com.szponty.recruitment_system.common.repository.FindOrThrowRepository;
import com.szponty.recruitment_system.dictionary.model.Location;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends FindOrThrowRepository<Location, UUID> {
    List<Location> findAllByDeletedFalse();

    Optional<Location> findByIdAndDeletedFalse(UUID locationId);
}
