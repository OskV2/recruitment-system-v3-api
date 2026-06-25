package com.szponty.recruitment_system.event.listener;

import com.szponty.recruitment_system.event.model.UserCreatedEvent;
import com.szponty.recruitment_system.event.model.UserDepartmentChangedEvent;
import com.szponty.recruitment_system.event.model.UserLockChangeEvent;
import com.szponty.recruitment_system.event.model.UserRoleChangeEvent;
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

    @EventListener
    public void handle(UserDepartmentChangedEvent event) {
        logService.createLog(
                event.createdBy(),
                "Department changed for user: " + event.firstName() +  " " + event.firstName() +
                        ". Previous department: " + event.prevDepartment() +
                        ". New department: " + event.newDepartment(),
                LogTrigger.USER_DEPARTMENT_CHANGED,
                LogType.INFO
        );
    }

    @EventListener
    public void handle(UserRoleChangeEvent event) {
        logService.createLog(
                event.createdBy(),
                "Role changed for user: " + event.firstName() +  " " + event.firstName() +
                        ". Previous role: " + event.prevRole() +
                        ". New role: " + event.newRole(),
                LogTrigger.USER_ROLE_CHANGED,
                LogType.INFO
        );
    }

    @EventListener
    public void handle(UserLockChangeEvent event) {

        String lockStateMessage = event.locked() ? "locked" : "unlocked";

        logService.createLog(
                event.createdBy(),
                event.firstName() +  " " + event.firstName() + " has been " + lockStateMessage,
                LogTrigger.USER_LOCK_CHANGE,
                LogType.WARNING
        );
    }
}
