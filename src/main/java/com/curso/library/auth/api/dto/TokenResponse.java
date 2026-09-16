package com.curso.library.auth.api.dto;

import com.curso.library.user.api.dto.UserResponse;

public record TokenResponse(
        String token,
        String tokenType,
        long expiresIn,
        UserResponse user
) {
}
