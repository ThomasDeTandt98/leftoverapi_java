package com.leftovr.leftoverapi.users.testSupport.users.application.dietaryPreferences;

import com.leftovr.leftoverapi.users.application.results.dietaryPreferences.DietaryPreferencesLookupResult;

import java.util.UUID;

public class DietaryPreferencesLookupResultTestBuilder {
    private UUID id = UUID.randomUUID();
    private String name = "Vegetarian";

    public static DietaryPreferencesLookupResultTestBuilder aDefault() {
        return new DietaryPreferencesLookupResultTestBuilder();
    }

    public DietaryPreferencesLookupResultTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public DietaryPreferencesLookupResultTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DietaryPreferencesLookupResult build() {
        return new DietaryPreferencesLookupResult(id, name);
    }
}
