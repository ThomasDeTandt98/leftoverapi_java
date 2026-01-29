package com.leftovr.leftoverapi.users.application.dietaryPreferences;

import com.leftovr.leftoverapi.users.application.services.dietaryPreferences.DietaryPreferencesService;
import com.leftovr.leftoverapi.users.domain.DietaryPreference;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeDietaryPreferencesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.infrastructure.DietaryPreferencesRepository;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import com.leftovr.leftoverapi.users.testSupport.users.domain.DietaryPreferenceTestBuilder;
import com.leftovr.leftoverapi.users.testSupport.users.domain.UserTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DietaryPreferencesServiceTest {

    DietaryPreferencesRepository dietaryPreferencesRepository = mock(DietaryPreferencesRepository.class);
    UserRepository userRepository = mock(UserRepository.class);
    DietaryPreferencesService dietaryPreferencesService = new DietaryPreferencesService(dietaryPreferencesRepository, userRepository);

    @Test
    void getDietaryPreferences_shouldReturnPreferences() {
        // Arrange
        List<DietaryPreference> preferences = List.of(
                DietaryPreferenceTestBuilder.aDefault().withName("Vegan").build(),
                DietaryPreferenceTestBuilder.aDefault().withName("Gluten-Free").build()
        );

        when(dietaryPreferencesRepository.findAllByIsActiveTrue()).thenReturn(preferences);

        // Act
        var result = dietaryPreferencesService.getLookupItems();

        // Assert
        assertEquals(result.size(), preferences.size());
        assertEquals(result.getFirst().name(), preferences.getFirst().getName());
        assertEquals(result.getLast().name(), preferences.getLast().getName());
    }

    @Test
    void getDietaryPreferences_shouldReturnEmptyList() {
        // Arrange
        when(dietaryPreferencesRepository.findAllByIsActiveTrue()).thenReturn(List.of());

        // Act
        var result = dietaryPreferencesService.getLookupItems();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void addUserDietaryPreferences_shouldThrowUserNotFoundException_WhenUserIsNotFound() {
        // Arrange
        String nonExistingUserId = "non-existing-user-id";
        Set<UUID> dietaryPreferenceIds = Set.of(UUID.randomUUID());
        when(userRepository.findById(nonExistingUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () ->
                dietaryPreferencesService.addUserDietaryPreferences(nonExistingUserId, dietaryPreferenceIds));

        verify(userRepository, times(1)).findById(nonExistingUserId);
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(dietaryPreferencesRepository);
    }

    @Test
    void addUserDietaryPreferences_shouldThrowSomeDietaryPreferencesNotFoundException_WhenDietaryPreferenceIsNotFound() {
        // Arrange
        String userId = "existing-user-id";
        User user = UserTestBuilder.aDefault().withId(userId).build();
        Set<UUID> dietaryPreferenceIds = Set.of(UUID.randomUUID());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dietaryPreferencesRepository.findByIdAndIsActiveTrue(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SomeDietaryPreferencesNotFoundException.class, () ->
                dietaryPreferencesService.addUserDietaryPreferences(userId, dietaryPreferenceIds));

        verify(userRepository, times(1)).findById(userId);
        verify(dietaryPreferencesRepository, times(dietaryPreferenceIds.size())).findByIdAndIsActiveTrue(any(UUID.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(dietaryPreferencesRepository);
    }

    @Test
    void addUserDietaryPreferences_shouldThrowError_whenDietaryPreferenceIsInactive() {
        // Arrange
        String userId = "existing-user-id";
        User user = UserTestBuilder.aDefault().withId(userId).build();
        UUID prefId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dietaryPreferencesRepository.findByIdAndIsActiveTrue(prefId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SomeDietaryPreferencesNotFoundException.class, () ->
                dietaryPreferencesService.addUserDietaryPreferences(userId, Set.of(prefId)));

        verify(userRepository, times(1)).findById(userId);
        verify(dietaryPreferencesRepository, times(1)).findByIdAndIsActiveTrue(prefId);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void addUserDietaryPreferences_shouldAddPreferences_whenRequestIsValid() {
        // Arrange
        String userId = "existing-user-id";
        User user = UserTestBuilder.aDefault().withId(userId).build();
        UUID prefId1 = UUID.randomUUID();
        UUID prefId2 = UUID.randomUUID();

        DietaryPreference pref1 = DietaryPreferenceTestBuilder.aDefault().withId(prefId1).withName("Vegan").build();
        DietaryPreference pref2 = DietaryPreferenceTestBuilder.aDefault().withId(prefId2).withName("Gluten-Free").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(dietaryPreferencesRepository.findByIdAndIsActiveTrue(prefId1)).thenReturn(Optional.of(pref1));
        when(dietaryPreferencesRepository.findByIdAndIsActiveTrue(prefId2)).thenReturn(Optional.of(pref2));

        // Act
        dietaryPreferencesService.addUserDietaryPreferences(userId, Set.of(prefId1, prefId2));

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(dietaryPreferencesRepository, times(1)).findByIdAndIsActiveTrue(prefId1);
        verify(dietaryPreferencesRepository, times(1)).findByIdAndIsActiveTrue(prefId2);
        assertTrue(user.getDietaryPreferences().contains(pref1));
        assertTrue(user.getDietaryPreferences().contains(pref2));
    }
}
