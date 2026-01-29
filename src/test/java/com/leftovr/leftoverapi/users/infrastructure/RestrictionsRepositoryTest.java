package com.leftovr.leftoverapi.users.infrastructure;

import com.leftovr.leftoverapi.users.domain.Restriction;
import com.leftovr.leftoverapi.users.testSupport.users.domain.RestrictionTestBuilder;
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
public class RestrictionsRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private RestrictionsRepository restrictionsRepository;

    @BeforeEach
    void setUp() {
        restrictionsRepository.deleteAll();
    }

    @Test
    void findAllByIsActiveTrue_shouldOnlyReturnActiveRestrictions() {
        // Arrange
        Restriction activeRestriction = RestrictionTestBuilder.aDefault().build();
        Restriction inactiveRestriction = RestrictionTestBuilder.aDefault().isActive(false).build();

        restrictionsRepository.save(activeRestriction);
        restrictionsRepository.save(inactiveRestriction);

        // Act
        List<Restriction> result = restrictionsRepository.findAllByIsActiveTrue();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void findAllByIsActiveTrue_shouldReturnEmptyListWhenNoActiveRestrictions() {
        // Arrange
        Restriction inactiveRestriction1 = RestrictionTestBuilder.aDefault().isActive(false).build();
        Restriction inactiveRestriction2 = RestrictionTestBuilder.aDefault().isActive(false).build();

        restrictionsRepository.save(inactiveRestriction1);
        restrictionsRepository.save(inactiveRestriction2);

        // Act
        List<Restriction> result = restrictionsRepository.findAllByIsActiveTrue();

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void findByIdAndIsActiveTrue_shouldReturnActiveRestrictionWhenExists() {
        // Arrange
        Restriction activeRestriction = RestrictionTestBuilder.aDefault().build();
        restrictionsRepository.save(activeRestriction);

        // Act
        var result = restrictionsRepository.findByIdAndIsActiveTrue(activeRestriction.getId());

        // Assert
        assertTrue(result.isPresent());
        assertEquals(activeRestriction.getId(), result.get().getId());
    }

    @Test
    void findByIdAndIsActiveTrue_shouldReturnEmptyWhenRestrictionIsInactive() {
        // Arrange
        Restriction inactiveRestriction = RestrictionTestBuilder.aDefault().isActive(false).build();
        restrictionsRepository.save(inactiveRestriction);

        // Act
        var result = restrictionsRepository.findByIdAndIsActiveTrue(inactiveRestriction.getId());

        // Assert
        assertTrue(result.isEmpty());
    }
}
