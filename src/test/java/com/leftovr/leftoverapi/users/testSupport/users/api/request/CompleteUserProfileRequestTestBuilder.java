package com.leftovr.leftoverapi.users.testSupport.users.api.request;

import com.leftovr.leftoverapi.users.api.requests.users.CompleteUserProfileRequest;

public class CompleteUserProfileRequestTestBuilder {
    private String firstName = "John";
    private String lastName = "Doe";

    public static CompleteUserProfileRequestTestBuilder aDefault() {
        return new CompleteUserProfileRequestTestBuilder();
    }

    public CompleteUserProfileRequestTestBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public CompleteUserProfileRequestTestBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CompleteUserProfileRequest build() {
        return new CompleteUserProfileRequest(firstName, lastName);
    }
}
