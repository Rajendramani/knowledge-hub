package com.knowledgehub.user.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record FullName(
        @Column(name = "first_name", nullable = false, length = 100) String firstName,
        @Column(name = "last_name", nullable = false, length = 100) String lastName) {
    public FullName {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty");
        firstName = firstName.trim();
        lastName = lastName.trim();
    }

    public String fullName() {
        return firstName + " " + lastName;
    }
}
