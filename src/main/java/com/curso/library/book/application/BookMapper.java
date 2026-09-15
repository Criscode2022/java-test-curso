package com.curso.library.book.application;

import com.curso.library.author.domain.Author;
import com.curso.library.book.api.dto.BookRequest;
import com.curso.library.book.api.dto.BookResponse;
import com.curso.library.book.domain.Book;

public final class BookMapper {

    private BookMapper() {
    }

    public static Book toEntity(BookRequest request, Author author) {
        return new Book(
                request.title().trim(),
                request.isbn().trim(),
                request.publishedYear(),
                blankToNull(request.genre()),
                request.pages(),
                author
        );
    }

    public static BookResponse toResponse(Book book) {
        Author author = book.getAuthor();
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getGenre(),
                book.getPages(),
                author.getId(),
                author.getName()
        );
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
