package com.leftovr.leftoverapi.users.testSupport.users.domain;

import com.leftovr.leftoverapi.users.domain.Restriction;

import java.util.UUID;

public class RestrictionTestBuilder {
    private UUID id = UUID.randomUUID();
    private String name = "Default Restriction Name";
    private boolean isActive = true;

    public static RestrictionTestBuilder aDefault() {
        return new RestrictionTestBuilder();
    }

    public RestrictionTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public RestrictionTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RestrictionTestBuilder isActive(boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public Restriction build() {
        return new Restriction(id, name, isActive);
    }
}
