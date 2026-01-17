package com.leftovr.leftoverapi.users.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SyncUserRequest(
        @Email(message = "Email must be a valid format")
        @NotBlank(message = "Email must be present")
        @Size(max = 255, message = "Email must be at most 255 characters long")
        String email,

        @NotBlank(message = "Username must be present")
        @Size(max = 255, message = "Username must be at most 255 characters long")
        String username
) {}