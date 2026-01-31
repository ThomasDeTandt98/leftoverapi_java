package com.leftovr.leftoverapi.users.testSupport.users.application.allergies;

import com.leftovr.leftoverapi.users.application.results.allergies.AllergiesLookupResult;

import java.util.UUID;

public class AllergiesLookupResultTestBuilder {
    private UUID uuid = UUID.randomUUID();
    private String name = "Default Allergy";

    public static AllergiesLookupResultTestBuilder aDefault() {
        return new AllergiesLookupResultTestBuilder();
    }

    public AllergiesLookupResultTestBuilder withUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public AllergiesLookupResultTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AllergiesLookupResult build() {
        return new AllergiesLookupResult(uuid, name);
    }
}
