package com.leftovr.leftoverapi.users.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SyncUserRequest(
        @Email(message = "Email must be a valid format")
        @NotNull @NotEmpty(message = "Email must be present")
        String email,

        @NotNull @NotEmpty(message = "Username must be present")
        @Size(max = 255, message = "Username must be at most 255 characters long")
        String username
) {}