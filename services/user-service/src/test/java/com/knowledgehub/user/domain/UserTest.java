package com.knowledgehub.user.domain;


import com.knowledgehub.user.domain.model.*;
import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;


class UserTest {


    @Test
    void shouldCreateUserWithDomainEvent() {
        User user = User.create(
            new Email("test@example.com"),
            new FullName("John", "Doe"),
            "hashed_password",
            UserRole.EDITOR
        );


        assertThat(user.getEmail().value()).isEqualTo("test@example.com");
        assertThat(user.getRole()).isEqualTo(UserRole.EDITOR);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getDomainEvents()).hasSize(1);
    }


    @Test
    void shouldNotDeactivateAdmin() {
        User admin = User.create(
            new Email("admin@example.com"),
            new FullName("Admin", "User"),
            "hashed",
            UserRole.ADMIN
        );


        assertThatThrownBy(admin::deactivate)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("Cannot deactivate an admin");
    }


    @Test
    void shouldRejectInvalidEmail() {
        assertThatThrownBy(() -> new Email("not-an-email"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
