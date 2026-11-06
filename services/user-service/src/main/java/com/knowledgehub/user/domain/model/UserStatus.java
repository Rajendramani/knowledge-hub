package com.knowledgehub.user.domain.model;

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    PENDING_VERIFICATION;

    public boolean isActive() {
        return this == ACTIVE;
    }

    // Java 21: Pattern matching in switch
    public String displayMessage() {
        return switch (this) {
            case ACTIVE -> "Account is active";
            case INACTIVE -> "Account has been deactivated";
            case SUSPENDED -> "Account is suspended - contact admin";
            case PENDING_VERIFICATION -> "Please verify your email";
        };
    }
}
