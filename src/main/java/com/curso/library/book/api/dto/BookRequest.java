package com.curso.library.book.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "title is required")
        @Size(max = 200, message = "title must be at most 200 characters")
        String title,

        @NotBlank(message = "author is required")
        @Size(max = 150, message = "author must be at most 150 characters")
        String author,

        @NotBlank(message = "isbn is required")
        @Size(min = 10, max = 17, message = "isbn must be between 10 and 17 characters")
        String isbn,

        @Min(value = 1400, message = "publishedYear must be 1400 or later")
        @Max(value = 2100, message = "publishedYear must be 2100 or earlier")
        Integer publishedYear
) {

    public static BookRequest empty() {
        return new BookRequest("", "", "", null);
    }

    public static BookRequest from(BookResponse book) {
        return new BookRequest(book.title(), book.author(), book.isbn(), book.publishedYear());
    }
}
