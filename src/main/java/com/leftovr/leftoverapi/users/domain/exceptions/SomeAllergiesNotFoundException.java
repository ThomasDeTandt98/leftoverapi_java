package com.leftovr.leftoverapi.users.domain.exceptions;

public class SomeAllergiesNotFoundException extends RuntimeException {
    public SomeAllergiesNotFoundException(String message) {
        super(message);
    }
}
