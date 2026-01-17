package com.leftovr.leftoverapi.users.api.requests;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CompleteUserProfileRequest(
        @NotBlank(message = "Frist name must be present")
        @Length(max = 255, message = "First name must be at most 255 characters long")
        String firstName,

        @NotBlank(message = "Last name must be present")
        @Length(max = 255, message = "Last name must be at most 255 characters long")
        String lastName) {
}
