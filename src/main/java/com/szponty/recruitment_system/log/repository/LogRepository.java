package com.szponty.recruitment_system.log.repository;

import com.szponty.recruitment_system.log.model.Log;
import com.szponty.recruitment_system.log.model.LogTrigger;
import com.szponty.recruitment_system.log.model.LogType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> { }








