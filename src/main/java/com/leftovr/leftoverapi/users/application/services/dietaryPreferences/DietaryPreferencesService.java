package com.leftovr.leftoverapi.users.application.services.dietaryPreferences;

import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;
import com.leftovr.leftoverapi.users.domain.DietaryPreference;
import com.leftovr.leftoverapi.users.domain.User;
import com.leftovr.leftoverapi.users.domain.exceptions.SomeDietaryPreferencesNotFoundException;
import com.leftovr.leftoverapi.users.domain.exceptions.UserNotFoundException;
import com.leftovr.leftoverapi.users.infrastructure.DietaryPreferencesRepository;
import com.leftovr.leftoverapi.users.infrastructure.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class DietaryPreferencesService {

    private final DietaryPreferencesRepository dietaryPreferencesRepository;
    private final UserRepository userRepository;

    public DietaryPreferencesService(DietaryPreferencesRepository dietaryPreferencesRepository, UserRepository userRepository) {
        this.dietaryPreferencesRepository = dietaryPreferencesRepository;
        this.userRepository = userRepository;
    }

    public List<DietaryPreferencesLookupResult> getLookupItems() {
        return dietaryPreferencesRepository.findAllByIsActiveTrue()
                .stream()
                .map(dp -> new DietaryPreferencesLookupResult(dp.getId(), dp.getName()))
                .toList();
    }

    @Transactional
    public void addUserDietaryPreferences(String userId, Set<UUID> dietaryPreferenceIds) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        HashSet<DietaryPreference> preferences = new HashSet<>();

        for (UUID dietaryPreferenceId : dietaryPreferenceIds) {
            DietaryPreference preference = dietaryPreferencesRepository.findByIdAndIsActiveTrue(dietaryPreferenceId)
                    .orElseThrow(() -> new SomeDietaryPreferencesNotFoundException("Some dietary preferences not found"));
            preferences.add(preference);
        }
        user.addDietaryPreferences(preferences);
    }
}