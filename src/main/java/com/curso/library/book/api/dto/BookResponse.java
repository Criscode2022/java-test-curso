package com.curso.library.book.api.dto;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publishedYear
) {
}
