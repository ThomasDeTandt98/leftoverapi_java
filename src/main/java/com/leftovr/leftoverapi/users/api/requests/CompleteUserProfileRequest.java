package com.leftovr.leftoverapi.users.api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompleteUserProfileRequest(
        @NotBlank(message = "Frist name must be present")
        @Size(max = 255, message = "First name must be at most 255 characters long")
        String firstName,

        @NotBlank(message = "Last name must be present")
        @Size(max = 255, message = "Last name must be at most 255 characters long")
        String lastName) {
}
