package com.curso.library.genre.api.dto;

public record GenreResponse(
        Long id,
        String name,
        String description,
        long bookCount
) {
}
