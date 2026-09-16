package com.curso.library.book.application;

import com.curso.library.author.domain.Author;
import com.curso.library.book.api.dto.BookRequest;
import com.curso.library.book.api.dto.BookResponse;
import com.curso.library.book.domain.Book;
import com.curso.library.genre.domain.Genre;
import com.curso.library.user.domain.User;

public final class BookMapper {

    private BookMapper() {
    }

    public static Book toEntity(BookRequest request, Author author, Genre genre, User owner) {
        return new Book(
                request.title().trim(),
                request.isbn().trim(),
                request.publishedYear(),
                request.pages(),
                author,
                genre,
                owner
        );
    }

    public static BookResponse toResponse(Book book) {
        Author author = book.getAuthor();
        Genre genre = book.getGenre();
        User owner = book.getOwner();
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublishedYear(),
                book.getPages(),
                author.getId(),
                author.getName(),
                genre.getId(),
                genre.getName(),
                owner.getId(),
                owner.getName()
        );
    }
}
