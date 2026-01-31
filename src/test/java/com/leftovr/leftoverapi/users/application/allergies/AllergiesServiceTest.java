package com.leftovr.leftoverapi.users.application.allergies;

import com.leftovr.leftoverapi.users.application.services.allergies.AllergiesService;
import com.leftovr.leftoverapi.users.domain.Allergy;
import com.leftovr.leftoverapi.users.infrastructure.AllergiesRepository;
import com.leftovr.leftoverapi.users.testSupport.users.domain.AllergyTestBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AllergiesServiceTest {

    AllergiesRepository allergiesRepository = mock(AllergiesRepository.class);
    AllergiesService allergiesService = new AllergiesService(allergiesRepository);

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
        assert(result.size() == allergies.size());
        assert(result.get(0).name().equals(allergies.get(0).getName()));
        assert(result.get(1).name().equals(allergies.get(1).getName()));
    }

    @Test
    void getAllergies_ShouldReturnEmptyList_WhenNoAllergiesFound() {
        // Arrange
        when(allergiesRepository.findAllByIsActiveIsTrue()).thenReturn(List.of());

        // Act
        var result = allergiesService.getLookupItems();

        // Assert
        assert(result.isEmpty());
    }
}
