package com.leftovr.leftoverapi.users.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {

        super("User not found with id: " + userId);
    }
}
