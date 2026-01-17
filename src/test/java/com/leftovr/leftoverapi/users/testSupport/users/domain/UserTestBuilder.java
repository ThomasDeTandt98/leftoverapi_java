package com.leftovr.leftoverapi.users.testSupport.users.domain;

import com.leftovr.leftoverapi.users.domain.User;

public class UserTestBuilder {
    private String id = "default-id";
    private String email = "test.user@leftovr.com";
    private String username = "testuser";
    private String firstName = "Test";
    private String lastName = "User";

    public static UserTestBuilder aUser() {
        return new UserTestBuilder();
    }

    public UserTestBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public UserTestBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserTestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User build() {
        var user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        return user;
    }
}