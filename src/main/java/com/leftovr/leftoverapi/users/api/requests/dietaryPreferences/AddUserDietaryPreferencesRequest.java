package com.leftovr.leftoverapi.users.api.requests.dietaryPreferences;

import java.util.Set;
import java.util.UUID;

public record AddUserDietaryPreferencesRequest(Set<UUID> dietaryPreferenceIds) {}