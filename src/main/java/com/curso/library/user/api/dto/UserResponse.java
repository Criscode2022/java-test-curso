package com.curso.library.user.api.dto;

public record UserResponse(
        Long id,
        String email,
        String name
) {
}
