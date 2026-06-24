package com.szponty.recruitment_system.event.listener;

import com.szponty.recruitment_system.event.model.UserCreatedEvent;
import com.szponty.recruitment_system.event.model.UserUpdatedEvent;
import com.szponty.recruitment_system.log.model.LogTrigger;
import com.szponty.recruitment_system.log.model.LogType;
import com.szponty.recruitment_system.log.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LogEventListener {

    private final LogService logService;

    @EventListener
    public void handle(UserCreatedEvent event) {
        logService.createLog(
                event.createdBy(),
                "New user created: " + event.firstName() + " " + event.lastName() +
                            ". Email: " + event.email() +
                            ". Assigned department: " + event.department().getName() +
                            ". Assigned role: " + event.role().getName(),
                LogTrigger.USER_CREATED,
                LogType.SUCCESS
        );
    }

    public void handle(UserUpdatedEvent event) {
        logService.createLog(
                event.createdBy(),
                "message",
                LogTrigger.USER_DEPARTMENT_CHANGED,
                LogType.INFO
        );
    }
}
