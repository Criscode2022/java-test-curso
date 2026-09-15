package com.curso.library.book.api.dto;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        Integer publishedYear,
        Integer pages,
        Long authorId,
        String authorName,
        Long genreId,
        String genreName
) {
}
