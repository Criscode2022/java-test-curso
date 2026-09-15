package com.curso.library.author.api.dto;

public record AuthorResponse(
        Long id,
        String name,
        String nationality,
        Integer birthYear,
        long bookCount
) {
}
