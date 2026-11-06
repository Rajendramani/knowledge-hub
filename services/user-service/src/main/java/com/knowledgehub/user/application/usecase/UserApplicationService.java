package com.knowledgehub.user.application.usecase;

import com.knowledgehub.user.application.dto.*;
import com.knowledgehub.user.application.mapper.UserMapper;
import com.knowledgehub.user.domain.model.*;
import com.knowledgehub.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserApplicationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    // ═══ CREATE USER ═══
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.email());

        // Check duplicate email
        Email email = new Email(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(request.email());
        }

        // Create domain entity via factory method
        UserRole role = request.role() != null
                ? UserRole.valueOf(request.role().toUpperCase())
                : UserRole.VIEWER;

        User user = User.create(
                email,
                new FullName(request.firstName(), request.lastName()),
                passwordEncoder.encode(request.password()),
                role);

        // Save
        User saved = userRepository.save(user);

        // Publish domain events
        saved.getDomainEvents().forEach(eventPublisher::publishEvent);
        saved.clearDomainEvents();

        log.info("User created successfully: {}", saved.getId());
        return userMapper.toResponse(saved);
    }

    // ═══ GET USER BY ID ═══
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    // ═══ SEARCH WITH PAGINATION ═══
    public Page<UserResponse> searchUsers(String query, Pageable pageable) {
        Page<User> usersPage = (query != null && !query.isBlank())
                ? userRepository.searchUsers(query, pageable)
                : userRepository.findAll(pageable);

        return usersPage.map(userMapper::toResponse);
    }

    // ═══ UPDATE PROFILE ═══
    @Transactional
    public UserResponse updateUser(UUID id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.updateProfile(
                new FullName(
                        request.firstName() != null ? request.firstName() : user.getName().firstName(),
                        request.lastName() != null ? request.lastName() : user.getName().lastName()),
                request.bio() != null ? request.bio() : user.getBio(),
                request.avatarUrl() != null ? request.avatarUrl() : user.getAvatarUrl());

        User saved = userRepository.save(user);
        saved.getDomainEvents().forEach(eventPublisher::publishEvent);
        saved.clearDomainEvents();

        return userMapper.toResponse(saved);
    }

    // ═══ DEACTIVATE USER ═══
    @Transactional
    public UserResponse deactivateUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.deactivate(); // Business rules enforced in Entity
        User saved = userRepository.save(user);
        saved.getDomainEvents().forEach(eventPublisher::publishEvent);
        saved.clearDomainEvents();
        return userMapper.toResponse(saved);
    }

    // ═══ JAVA STREAMS DEMO: Aggregate Statistics ═══
    public Map<String, Long> getUserStatsByRole() {
        return userRepository.findAllByStatusAsList(UserStatus.ACTIVE)
                .stream()
                .collect(Collectors.groupingBy(
                        user -> user.getRole().name(),
                        Collectors.counting()));
    }
}
