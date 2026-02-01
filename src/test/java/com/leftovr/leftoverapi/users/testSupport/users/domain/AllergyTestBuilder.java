package com.leftovr.leftoverapi.users.testSupport.users.domain;

import com.leftovr.leftoverapi.users.domain.Allergy;

import java.util.UUID;

public class AllergyTestBuilder {
    private UUID id = UUID.randomUUID();
    private String name = "Default Allergy";
    private boolean isActive = true;

    public static AllergyTestBuilder aDefault() {
        return new AllergyTestBuilder();
    }

    public AllergyTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public AllergyTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AllergyTestBuilder withIsActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Allergy build() {
        return new Allergy(id, name, isActive);
    }
}
