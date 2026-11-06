package com.knowledgehub.user.domain.repository;

import com.knowledgehub.user.domain.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(Email email);

    boolean existsByEmail(Email email);

    Page<User> findByRole(UserRole role, Pageable pageable);

    Page<User> findByStatus(UserStatus status, Pageable pageable);

    // Custom JPQL query - searching by name
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.name.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(u.email.value) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<User> searchUsers(@Param("query") String query, Pageable pageable);

    // Java Stream query - demonstrates Stream API with JPA
    @Query("SELECT u FROM User u WHERE u.status = :status")
    List<User> findAllByStatusAsList(@Param("status") UserStatus status);
}
