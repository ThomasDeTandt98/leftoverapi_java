package com.leftovr.leftoverapi.users.api.responses;

import com.leftovr.leftoverapi.users.domain.User;

public record UserResponse(
        String id,
        String email,
        String username,
        String firstName,
        String lastName
) {
    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
