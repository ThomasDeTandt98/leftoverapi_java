package com.leftovr.leftoverapi.users.application.services.dietaryPreferences;

import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;
import com.leftovr.leftoverapi.users.infrastructure.DietaryPreferencesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietaryPreferencesService {

    private final DietaryPreferencesRepository dietaryPreferencesRepository;

    public DietaryPreferencesService(DietaryPreferencesRepository dietaryPreferencesRepository) {
        this.dietaryPreferencesRepository = dietaryPreferencesRepository;
    }

    public List<DietaryPreferencesLookupResult> getLookupItems() {
        return dietaryPreferencesRepository.findAllByIsActiveTrue()
                .stream()
                .map(dp -> new DietaryPreferencesLookupResult(dp.getId(), dp.getName()))
                .toList();
    }
}
