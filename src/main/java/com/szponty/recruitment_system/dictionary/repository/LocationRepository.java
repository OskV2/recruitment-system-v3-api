package com.szponty.recruitment_system.dictionary.repository;

import com.szponty.recruitment_system.dictionary.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
}
