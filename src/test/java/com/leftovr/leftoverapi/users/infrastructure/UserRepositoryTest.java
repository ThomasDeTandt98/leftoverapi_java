package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    UserRepository userRepository;

    @Test
    void saveUser_shouldSaveUser() {
        // Arrange
        var user = UserTestBuilder.aDefault().build();

        // Act
        var savedUser = userRepository.save(user);

        // Assert
        var retrievedUser = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new AssertionError("User not found"));

        assertThat(retrievedUser.getId()).isEqualTo(savedUser.getId());
        assertThat(retrievedUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(retrievedUser.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(retrievedUser.getFirstName()).isEqualTo(savedUser.getFirstName());
        assertThat(retrievedUser.getLastName()).isEqualTo(savedUser.getLastName());
    }
}
