package com.szponty.recruitment_system.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entityName, Object id) {
        super(entityName + " with id " + id + " not found");
    }
}