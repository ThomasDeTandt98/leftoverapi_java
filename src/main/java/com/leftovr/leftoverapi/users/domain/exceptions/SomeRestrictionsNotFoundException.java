package com.leftovr.leftoverapi.users.domain.exceptions;

public class SomeRestrictionsNotFoundException extends RuntimeException {
    public SomeRestrictionsNotFoundException(String message) {
        super(message);
    }
}
