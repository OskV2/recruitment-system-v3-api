package com.szponty.recruitment_system.event.model;

import java.util.UUID;

public record UserCreatedEvent(UUID userId, String nameOfUser) {

}
