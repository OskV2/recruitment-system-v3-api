package com.szponty.recruitment_system.log.service;

import com.szponty.recruitment_system.log.model.Log;
import com.szponty.recruitment_system.log.model.LogTrigger;
import com.szponty.recruitment_system.log.model.LogType;
import com.szponty.recruitment_system.log.repository.LogRepository;
import com.szponty.recruitment_system.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void createLog(User createdBy, String message, LogTrigger trigger, LogType type) {
        Log log = Log.builder()
                .createdBy(createdBy)
                .message(message)
                .trigger(String.valueOf(trigger))
                .type(String.valueOf(type))
                .build();

        logRepository.save(log);
    }
}
