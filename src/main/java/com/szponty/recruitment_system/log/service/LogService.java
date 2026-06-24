package com.szponty.recruitment_system.log.service;

import com.szponty.recruitment_system.log.model.Log;
import com.szponty.recruitment_system.log.model.LogTrigger;
import com.szponty.recruitment_system.log.model.LogType;
import com.szponty.recruitment_system.log.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void createLog(String message, LogTrigger trigger, LogType type) {
        logRepository.createLog(message, trigger, type);
    }
}
