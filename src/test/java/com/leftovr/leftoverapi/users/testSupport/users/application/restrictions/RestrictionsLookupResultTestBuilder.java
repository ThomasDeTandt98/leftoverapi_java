package com.leftovr.leftoverapi.users.testSupport.users.application.restrictions;

import com.leftovr.leftoverapi.users.application.results.restrictions.RestrictionsLookupResult;

import java.util.UUID;

public class RestrictionsLookupResultTestBuilder {
    private UUID id = UUID.randomUUID();
    private String name = "Default Restriction";

    public static RestrictionsLookupResultTestBuilder aDefault() {
        return new RestrictionsLookupResultTestBuilder();
    }

    public RestrictionsLookupResultTestBuilder withId(UUID id) {
        this.id = id;
        return this;
    }

    public RestrictionsLookupResultTestBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public RestrictionsLookupResult build() {
        return new RestrictionsLookupResult(id, name);
    }
}
