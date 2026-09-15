package com.curso.library.book.api.dto;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        Integer publishedYear,
        String genre,
        Integer pages,
        Long authorId,
        String authorName
) {
}
