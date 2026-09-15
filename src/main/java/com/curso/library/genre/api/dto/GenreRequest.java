package com.curso.library.genre.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequest(
        @NotBlank(message = "name is required")
        @Size(max = 80, message = "name must be at most 80 characters")
        String name,

        @Size(max = 200, message = "description must be at most 200 characters")
        String description
) {

    public static GenreRequest empty() {
        return new GenreRequest("", "");
    }

    public static GenreRequest from(GenreResponse genre) {
        return new GenreRequest(genre.name(), genre.description());
    }
}
