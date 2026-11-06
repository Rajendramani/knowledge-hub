package com.knowledgehub.user.domain.model;

import com.knowledgehub.common.domain.BaseEntity;
import com.knowledgehub.user.domain.event.*;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;

/**
 * User Aggregate Root.
 * ALL user mutations go through this class.
 * Business rules are enforced HERE, not in services.
 */
@Entity
@Table(name = "users", schema = "user_service")
public class User extends BaseEntity {

    @Embedded
    private Email email;

    @Embedded
    private FullName name;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    // Domain events (not persisted - transient)
    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    // ═══ CONSTRUCTORS ═══
    protected User() {
    } // JPA requires no-arg constructor

    // Factory method - the ONLY way to create a User
    public static User create(Email email, FullName name,
            String passwordHash, UserRole role) {
        User user = new User();
        user.email = email;
        user.name = name;
        user.passwordHash = passwordHash;
        user.role = role;
        user.status = UserStatus.ACTIVE;
        user.registerEvent(new UserCreatedEvent(user));
        return user;
    }

    // ═══ BUSINESS METHODS (domain logic lives HERE) ═══

    public void updateProfile(FullName newName, String bio, String avatarUrl) {
        this.name = newName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        registerEvent(new UserUpdatedEvent(this));
    }

    public void changeRole(UserRole newRole, User requestedBy) {
        // Business Rule: Only ADMIN can change roles
        if (!requestedBy.getRole().canManageUsers()) {
            throw new IllegalStateException(
                    "Only admins can change user roles");
        }
        // Business Rule: Cannot demote yourself
        if (this.equals(requestedBy) && newRole != UserRole.ADMIN) {
            throw new IllegalStateException(
                    "Admin cannot demote themselves");
        }
        this.role = newRole;
        registerEvent(new UserUpdatedEvent(this));
    }

    public void deactivate() {
        if (this.status == UserStatus.INACTIVE) {
            throw new IllegalStateException(
                    "User is already deactivated");
        }
        if (this.role == UserRole.ADMIN) {
            throw new IllegalStateException(
                    "Cannot deactivate an admin account");
        }
        this.status = UserStatus.INACTIVE;
        registerEvent(new UserDeactivatedEvent(this));
    }

    public void reactivate() {
        if (this.status == UserStatus.ACTIVE) {
            throw new IllegalStateException("User is already active");
        }
        this.status = UserStatus.ACTIVE;
        registerEvent(new UserUpdatedEvent(this));
    }

    public void recordLogin() {
        this.lastLoginAt = Instant.now();
    }

    // ═══ DOMAIN EVENT MANAGEMENT ═══
    private void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // ═══ GETTERS (no public setters - enforce invariants) ═══
    public Email getEmail() {
        return email;
    }

    public FullName getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserRole getRole() {
        return role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public String getBio() {
        return bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }
}
