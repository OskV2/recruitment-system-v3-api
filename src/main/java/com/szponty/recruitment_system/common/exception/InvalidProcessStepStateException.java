package com.szponty.recruitment_system.common.exception;

public class InvalidProcessStepStateException extends RuntimeException {
    public InvalidProcessStepStateException(String message) {
        super(message);
    }
}
