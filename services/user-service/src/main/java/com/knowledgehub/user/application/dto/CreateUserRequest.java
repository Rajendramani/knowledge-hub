package com.knowledgehub.user.application.dto;


import jakarta.validation.constraints.*;


/**
 * Java 21 Record as DTO.
 * Immutable. Auto-generates constructor, getters, equals, hashCode, toString.
 * Jakarta Validation annotations work with Records.
 */
public record CreateUserRequest(
    @NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Invalid email format")
    String email,


    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 100, message = "First name must be 2-100 chars")
    String firstName,


    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 100, message = "Last name must be 2-100 chars")
    String lastName,


    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    String password,


    String role  // Optional, defaults to VIEWER
) {}
