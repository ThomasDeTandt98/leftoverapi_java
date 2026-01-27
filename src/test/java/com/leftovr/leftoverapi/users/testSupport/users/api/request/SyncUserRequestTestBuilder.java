package com.leftovr.leftoverapi.users.testSupport.users.api.request;

import com.leftovr.leftoverapi.users.api.requests.users.SyncUserRequest;

public class SyncUserRequestTestBuilder {
    private String username = "testuser";
    private String email = "test.user@leftovr.com";

    public static SyncUserRequestTestBuilder aDefault() {
        return new SyncUserRequestTestBuilder();
    }

    public SyncUserRequestTestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public SyncUserRequestTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public SyncUserRequest build() {
        return new SyncUserRequest(email, username);
    }
}