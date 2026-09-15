package com.curso.library.author.application;

import com.curso.library.author.api.dto.AuthorRequest;
import com.curso.library.author.api.dto.AuthorResponse;
import com.curso.library.author.domain.Author;

public final class AuthorMapper {

    private AuthorMapper() {
    }

    public static Author toEntity(AuthorRequest request) {
        return new Author(
                request.name().trim(),
                blankToNull(request.nationality()),
                request.birthYear()
        );
    }

    public static AuthorResponse toResponse(Author author, long bookCount) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getNationality(),
                author.getBirthYear(),
                bookCount
        );
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
