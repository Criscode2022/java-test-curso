package com.curso.library.author.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
        @NotBlank(message = "name is required")
        @Size(max = 150, message = "name must be at most 150 characters")
        String name,

        @Size(max = 80, message = "nationality must be at most 80 characters")
        String nationality,

        @Min(value = 1400, message = "birthYear must be 1400 or later")
        @Max(value = 2100, message = "birthYear must be 2100 or earlier")
        Integer birthYear
) {

    public static AuthorRequest empty() {
        return new AuthorRequest("", "", null);
    }

    public static AuthorRequest from(AuthorResponse author) {
        return new AuthorRequest(author.name(), author.nationality(), author.birthYear());
    }
}
