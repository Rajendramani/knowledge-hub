package com.knowledgehub.user.domain.event;

import com.knowledgehub.user.domain.model.User;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain Event: Published when a new user is created.
 * Java 21 Record - perfect for immutable event data.
 */
public record UserCreatedEvent(
        UUID userId,
        String email,
        String fullName,
        String role,
        Instant occurredAt) {
    public UserCreatedEvent(User user) {
        this(
                user.getId(),
                user.getEmail().value(),
                user.getName().fullName(),
                user.getRole().name(),
                Instant.now());
    }
}
