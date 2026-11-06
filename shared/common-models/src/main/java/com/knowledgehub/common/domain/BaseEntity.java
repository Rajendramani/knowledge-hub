package com.knowledgehub.common.domain;


import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;


@MappedSuperclass
public abstract class BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;


    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


    @Version
    private Long version;


    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }


    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }


    // Getters
    public UUID getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }


    // Equality based on ID (DDD Entity rule)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && id.equals(that.id);
    }


    @Override
    public int hashCode() {
        return getClass().hashCode(); // Consistent before and after persist
    }
}
