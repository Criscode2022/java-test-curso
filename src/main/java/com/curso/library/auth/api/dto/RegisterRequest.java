package com.curso.library.auth.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "name is required")
        @Size(max = 80, message = "name must be at most 80 characters")
        String name,

        @NotBlank(message = "email is required")
        @Email(message = "email must be valid")
        @Size(max = 180, message = "email must be at most 180 characters")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 6, max = 72, message = "password must be between 6 and 72 characters")
        String password
) {

    public static RegisterRequest empty() {
        return new RegisterRequest("", "", "");
    }
}
