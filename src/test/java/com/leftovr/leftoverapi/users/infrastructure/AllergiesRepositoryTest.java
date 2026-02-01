package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.domain.Allergy;
import com.leftovr.leftoverapi.users.testSupport.users.domain.AllergyTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
public class AllergiesRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private AllergiesRepository allergiesRepository;

    @BeforeEach
    public void setup() {
        allergiesRepository.deleteAll();
    }

    @Test
    void findAllByIsActiveIsTrue_shouldOnlyReturnActiveAllergies() {
        // Arrange
        Allergy acticeAllergy = AllergyTestBuilder.aDefault().build();
        Allergy inactiveAllergy = AllergyTestBuilder.aDefault().withIsActive(false).build();

        allergiesRepository.save(acticeAllergy);
        allergiesRepository.save(inactiveAllergy);

        // Act
        List<Allergy> result = allergiesRepository.findAllByIsActiveIsTrue();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findAllByIsActiveIsTrue_shouldReturnEmptyListWhenNoActiveAllergies() {
        // Arrange
        Allergy inactiveAllergy1 = AllergyTestBuilder.aDefault().withIsActive(false).build();
        Allergy inactiveAllergy2 = AllergyTestBuilder.aDefault().withIsActive(false).build();

        allergiesRepository.save(inactiveAllergy1);
        allergiesRepository.save(inactiveAllergy2);

        // Act
        List<Allergy> result = allergiesRepository.findAllByIsActiveIsTrue();

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByIdAndIsActiveIsTrue_shouldReturnAllergyWhenActive() {
        // Arrange
        Allergy activeAllergy = AllergyTestBuilder.aDefault().build();
        allergiesRepository.save(activeAllergy);

        // Act
        var result = allergiesRepository.findByIdAndIsActiveIsTrue(activeAllergy.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(activeAllergy.getName(), result.get().getName());
    }

    @Test
    void findByIdAndIsActiveIsTrue_shouldReturnEmptyWhenAllergyIsInactive() {
        // Arrange
        Allergy inactiveAllergy = AllergyTestBuilder.aDefault().withIsActive(false).build();
        allergiesRepository.save(inactiveAllergy);

        // Act
        var result = allergiesRepository.findByIdAndIsActiveIsTrue(inactiveAllergy.getId());

        // Assert
        assertTrue(result.isEmpty());
    }
}
