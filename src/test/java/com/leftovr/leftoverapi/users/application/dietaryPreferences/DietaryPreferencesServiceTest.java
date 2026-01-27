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

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assert (result.size() == 2);
        assert (result.get(0).name().equals("Vegan"));
        assert (result.get(1).name().equals("Gluten-Free"));
    }

    @Test
    void getDietaryPreferences_shouldReturnEmptyList() {
        // Arrange
        when(dietaryPreferencesRepository.findAllByIsActiveTrue()).thenReturn(List.of());

        // Act
        var result = dietaryPreferencesService.getLookupItems();

        // Assert
        assert (result.isEmpty());
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
        when(dietaryPreferencesRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SomeDietaryPreferencesNotFoundException.class, () ->
                dietaryPreferencesService.addUserDietaryPreferences(userId, dietaryPreferenceIds));

        verify(userRepository, times(1)).findById(userId);
        verify(dietaryPreferencesRepository, times(dietaryPreferenceIds.size())).findById(any(UUID.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(dietaryPreferencesRepository);
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
        when(dietaryPreferencesRepository.findById(prefId1)).thenReturn(Optional.of(pref1));
        when(dietaryPreferencesRepository.findById(prefId2)).thenReturn(Optional.of(pref2));

        // Act
        dietaryPreferencesService.addUserDietaryPreferences(userId, Set.of(prefId1, prefId2));

        // Assert
        verify(userRepository, times(1)).findById(userId);
        verify(dietaryPreferencesRepository, times(1)).findById(prefId1);
        verify(dietaryPreferencesRepository, times(1)).findById(prefId2);
        assert(user.getDietaryPreferences().contains(pref1));
        assert(user.getDietaryPreferences().contains(pref2));
    }
}
