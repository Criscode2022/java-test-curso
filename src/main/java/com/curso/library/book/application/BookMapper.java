package com.curso.library.book.application;

import com.curso.library.book.api.dto.BookRequest;
import com.curso.library.book.api.dto.BookResponse;
import com.curso.library.book.domain.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static Book toEntity(BookRequest request) {
        return new Book(
                request.title().trim(),
                request.author().trim(),
                request.isbn().trim(),
                request.publishedYear()
        );
    }

    public static BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear()
        );
    }
}
