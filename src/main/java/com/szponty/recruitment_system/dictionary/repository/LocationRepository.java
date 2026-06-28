package com.szponty.recruitment_system.dictionary.repository;

import com.szponty.recruitment_system.dictionary.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    List<Location> findAllByDeletedFalse();

    Optional<Location> findByIdAndDeletedFalse();
}
