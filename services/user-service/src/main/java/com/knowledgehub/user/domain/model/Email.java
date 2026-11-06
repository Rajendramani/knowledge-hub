package com.knowledgehub.user.domain.model;


import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;


/**
 * Email Value Object - immutable, self-validating.
 * DDD Value Object: equality by value, not identity.
 */
@Embeddable
public record Email(String value) {


    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");


    // Compact constructor - validates on creation
    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        value = value.toLowerCase().trim();
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
    }


    @Override
    public String toString() {
        return value;
    }
}
