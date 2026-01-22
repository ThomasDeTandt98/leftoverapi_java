package com.leftovr.leftoverapi.users.testSupport.users.domain;

import com.leftovr.leftoverapi.users.domain.DietaryPreference;

import java.util.UUID;

public class DietaryPreferenceTestBuilder {
    private UUID id = UUID.randomUUID();
    private String name = "Vegetarian";
    private boolean isActive = true;

    public static DietaryPreferenceTestBuilder aDefault() {
        return new DietaryPreferenceTestBuilder();
    }

    public DietaryPreferenceTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public DietaryPreferenceTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DietaryPreferenceTestBuilder withActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public DietaryPreference build() {
        return new DietaryPreference(id, name, isActive);
    }
}
