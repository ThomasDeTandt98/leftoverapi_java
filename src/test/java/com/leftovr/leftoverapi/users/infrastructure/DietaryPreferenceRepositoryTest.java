package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.domain.DietaryPreference;
import com.leftovr.leftoverapi.users.testSupport.users.domain.DietaryPreferenceTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
public class DietaryPreferenceRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    DietaryPreferencesRepository dietaryPreferencesRepository;

    @BeforeEach
    void setUp() {
        dietaryPreferencesRepository.deleteAll();
    }

    @Test
    void findAllByIsActiveTrue_shouldReturnOnlyActiveDietaryPreferences() {
        // Arrange
        DietaryPreference activePreference = DietaryPreferenceTestBuilder.aDefault().withName("Active").build();
        DietaryPreference inactivePreference = DietaryPreferenceTestBuilder.aDefault().withName("Inactive").withActive(false).build();

        dietaryPreferencesRepository.save(activePreference);
        dietaryPreferencesRepository.save(inactivePreference);

        // Act
        var activePreferences = dietaryPreferencesRepository.findAllByIsActiveTrue();

        // Assert
        assertEquals(1, activePreferences.size());
        assertEquals("Active", activePreferences.getFirst().getName());
    }

    @Test
    void findAllByIsActiveTrue_shouldReturnEmptyListWhenNoActiveDietaryPreferences() {
        // Arrange
        DietaryPreference inactivePreference1 = DietaryPreferenceTestBuilder.aDefault().withName("Inactive1").withActive(false).build();
        DietaryPreference inactivePreference2 = DietaryPreferenceTestBuilder.aDefault().withName("Inactive2").withActive(false).build();

        dietaryPreferencesRepository.save(inactivePreference1);
        dietaryPreferencesRepository.save(inactivePreference2);

        // Act
        var activePreferences = dietaryPreferencesRepository.findAllByIsActiveTrue();

        // Assert
        assertTrue(activePreferences.isEmpty());
    }
}
