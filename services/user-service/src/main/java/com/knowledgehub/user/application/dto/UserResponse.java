package com.knowledgehub.user.application.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String fullName,
        String role,
        String status,
        String bio,
        String avatarUrl,
        Instant lastLoginAt,
        Instant createdAt,
        Instant updatedAt) {
}
