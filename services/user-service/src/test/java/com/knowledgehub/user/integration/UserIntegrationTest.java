package com.knowledgehub.user.integration;

import com.knowledgehub.user.application.dto.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.schemas", () -> "user_service");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndRetrieveUser() {
        // Create
        CreateUserRequest request = new CreateUserRequest(
                "integration@test.com", "Jane", "Doe",
                "securepass123", "EDITOR");
        ResponseEntity<UserResponse> createResp = restTemplate
                .postForEntity("/api/v1/users", request, UserResponse.class);

        Assertions.assertEquals(HttpStatus.CREATED, createResp.getStatusCode());
        Assertions.assertNotNull(createResp.getBody());
        Assertions.assertNotNull(createResp.getBody().id());

        // Retrieve
        ResponseEntity<UserResponse> getResp = restTemplate.getForEntity(
                "/api/v1/users/" + createResp.getBody().id(),
                UserResponse.class);

        Assertions.assertEquals(HttpStatus.OK, getResp.getStatusCode());
        Assertions.assertEquals("integration@test.com",
                getResp.getBody().email());
    }
}
