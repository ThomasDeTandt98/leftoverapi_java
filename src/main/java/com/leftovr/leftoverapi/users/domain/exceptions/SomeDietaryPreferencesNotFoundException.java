package com.leftovr.leftoverapi.users.domain.exceptions;

public class SomeDietaryPreferencesNotFoundException extends RuntimeException {
    public SomeDietaryPreferencesNotFoundException(String message) {
        super(message);
    }
}
