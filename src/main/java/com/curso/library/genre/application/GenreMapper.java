package com.curso.library.genre.application;

import com.curso.library.genre.api.dto.GenreRequest;
import com.curso.library.genre.api.dto.GenreResponse;
import com.curso.library.genre.domain.Genre;

public final class GenreMapper {

    private GenreMapper() {
    }

    public static Genre toEntity(GenreRequest request) {
        return new Genre(request.name().trim(), blankToNull(request.description()));
    }

    public static GenreResponse toResponse(Genre genre, long bookCount) {
        return new GenreResponse(genre.getId(), genre.getName(), genre.getDescription(), bookCount);
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
