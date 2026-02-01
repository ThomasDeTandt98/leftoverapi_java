package com.leftovr.leftoverapi.users.application.allergies;

import com.leftovr.leftoverapi.users.application.services.allergies.AllergiesService;
import com.leftovr.leftoverapi.users.domain.Allergy;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeAllergiesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.infrastructure.AllergiesRepository;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import com.leftovr.leftoverapi.users.testSupport.users.domain.AllergyTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AllergiesServiceTest {

    AllergiesRepository allergiesRepository = mock(AllergiesRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    AllergiesService allergiesService = new AllergiesService(allergiesRepository, userRepository);

    @Test
    void getAllergies_ShouldReturnAllergies() {
        // Arrange
        List<Allergy> allergies = List.of(
                AllergyTestBuilder.aDefault().withName("nuts").build(),
                AllergyTestBuilder.aDefault().withName("dairy").build()
        );

        when(allergiesRepository.findAllByIsActiveIsTrue()).thenReturn(allergies);

        // Act
        var result = allergiesService.getLookupItems();

        // Assert
        assert (result.size() == allergies.size());
        assert (result.get(0).name().equals(allergies.get(0).getName()));
        assert (result.get(1).name().equals(allergies.get(1).getName()));
    }

    @Test
    void getAllergies_ShouldReturnEmptyList_WhenNoAllergiesFound() {
        // Arrange
        when(allergiesRepository.findAllByIsActiveIsTrue()).thenReturn(List.of());

        // Act
        var result = allergiesService.getLookupItems();

        // Assert
        assert (result.isEmpty());
    }

    @Test
    void addUserDietaryPreferences_shouldThrowUserNotFoundException_WhenUserIsNotFound() {
        // Arrange
        String userId = "non-existent-user-id";

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                allergiesService.addUserAllergies(userId, Set.of()));

        verify(userRepository, times(1)).findById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(allergiesRepository);
    }

    @Test
    void adduserAllergies_ThrowsSomeAllergiesNotFoundException_WhenSomeAllergiesNotFound() {
        // Arrange
        String userId = "existing-user-id";
        User user = UserTestBuilder.aDefault().build();
        var existingAllergyId = AllergyTestBuilder.aDefault().withName("Pollen").build().getId();
        var nonExistingAllergyId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(allergiesRepository.findByIdAndIsActiveIsTrue(existingAllergyId)).thenReturn(
                Optional.of(AllergyTestBuilder.aDefault().build()));
        when(allergiesRepository.findByIdAndIsActiveIsTrue(nonExistingAllergyId)).thenReturn(
                Optional.empty());

        // Act & Assert
        assertThrows(SomeAllergiesNotFoundException.class, () ->
                allergiesService.addUserAllergies(userId, Set.of(existingAllergyId, nonExistingAllergyId)));

        verify(userRepository, times(1)).findById(userId);
        verify(allergiesRepository, atLeastOnce()).findByIdAndIsActiveIsTrue(any(UUID.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(allergiesRepository);
    }

    @Test
    void addUserAllergies_ShouldAddAllergiesToUser_WhenAllergiesExist() {
        // Arrange
        String userId = "existing-user-id";
        User user = UserTestBuilder.aDefault().build();
        var allergyId1 = AllergyTestBuilder.aDefault().withName("Pollen").build().getId();
        var allergyId2 = AllergyTestBuilder.aDefault().withName("Dust").build().getId();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(allergiesRepository.findByIdAndIsActiveIsTrue(allergyId1)).thenReturn(
                Optional.of(AllergyTestBuilder.aDefault().withName("Pollen").build()));
        when(allergiesRepository.findByIdAndIsActiveIsTrue(allergyId2)).thenReturn(
                Optional.of(AllergyTestBuilder.aDefault().withName("Dust").build()));

        // Act
        allergiesService.addUserAllergies(userId, Set.of(allergyId1, allergyId2));

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(allergiesRepository, times(1)).findByIdAndIsActiveIsTrue(allergyId1);
        verify(allergiesRepository, times(1)).findByIdAndIsActiveIsTrue(allergyId2);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(allergiesRepository);
    }
}
