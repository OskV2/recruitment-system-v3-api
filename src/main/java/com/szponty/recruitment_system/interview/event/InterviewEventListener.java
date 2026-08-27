package com.szponty.recruitment_system.interview.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class InterviewEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onInterviewRescheduled(InterviewRescheduledEvent event) {
        // TODO: in next PR -> get JobApplication, create EmailMessage
        //  then call emailService.sendInterviewRescheduledEmail()
        log.info("Interview {} rescheduled, candidate notification pending: {}", event.interviewId(), event);
    }
}
