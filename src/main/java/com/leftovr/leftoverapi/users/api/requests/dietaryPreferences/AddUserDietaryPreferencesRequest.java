package com.leftovr.leftoverapi.users.api.requests.dietaryPreferences;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record AddUserDietaryPreferencesRequest(
        @NotEmpty(message = "Dietary preference IDs must be provided")
        Set<UUID> dietaryPreferenceIds) {}