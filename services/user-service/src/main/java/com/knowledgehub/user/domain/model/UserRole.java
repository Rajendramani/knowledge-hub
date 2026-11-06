package com.knowledgehub.user.domain.model;

/**
 * Sealed interface restricts which classes can implement it.
 * Java 21: Enables exhaustive pattern matching in switch.
 */
public enum UserRole {
    ADMIN,
    EDITOR,
    VIEWER;

    /**
     * Java 21 Pattern Matching with switch expression.
     * Compiler ensures ALL cases are handled (exhaustive).
     */
    public boolean canEdit() {
        return switch (this) {
            case ADMIN, EDITOR -> true;
            case VIEWER -> false;
        };
    }

    public boolean canDelete() {
        return this == ADMIN;
    }

    public boolean canManageUsers() {
        return this == ADMIN;
    }
}
