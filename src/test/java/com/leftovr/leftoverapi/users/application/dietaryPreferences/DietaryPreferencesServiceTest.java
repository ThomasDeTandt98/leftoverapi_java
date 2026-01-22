package com.leftovr.leftoverapi.users.application.dietaryPreferences;

import com.leftovr.leftoverapi.users.application.services.dietaryPreferences.DietaryPreferencesService;
import com.leftovr.leftoverapi.users.domain.DietaryPreference;
import com.leftovr.leftoverapi.users.infrastructure.DietaryPreferencesRepository;
import com.leftovr.leftoverapi.users.testSupport.users.domain.DietaryPreferenceTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DietaryPreferencesServiceTest {

    DietaryPreferencesRepository dietaryPreferencesRepository = mock(DietaryPreferencesRepository.class);
    DietaryPreferencesService dietaryPreferencesService = new DietaryPreferencesService(dietaryPreferencesRepository);

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
}
