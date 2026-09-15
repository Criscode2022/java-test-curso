package com.curso.library.book.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.curso.library.author.domain.Author;
import com.curso.library.author.domain.AuthorRepository;
import com.curso.library.book.api.dto.BookRequest;
import com.curso.library.book.api.dto.BookResponse;
import com.curso.library.book.domain.Book;
import com.curso.library.book.domain.BookRepository;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.common.error.ResourceNotFoundException;
import com.curso.library.genre.domain.Genre;
import com.curso.library.genre.domain.GenreRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    private BookService bookService;
    private Author author;
    private Genre genre;

    @BeforeEach
    void setUp() {
        bookService = new BookService(bookRepository, authorRepository, genreRepository);
        author = new Author("Robert C. Martin", "United States", 1952);
        genre = new Genre("Software", "Programming and craft.");
    }

    @Test
    void createSavesBookWhenIsbnIsFree() {
        BookRequest request = new BookRequest("Clean Code", "9780132350884", 2008, 1L, 2L, 464);
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findById(2L)).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookResponse response = bookService.create(request);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Clean Code");
        assertThat(captor.getValue().getAuthor().getName()).isEqualTo("Robert C. Martin");
        assertThat(captor.getValue().getGenre().getName()).isEqualTo("Software");
        assertThat(response.title()).isEqualTo("Clean Code");
    }

    @Test
    void createRejectsDuplicateIsbn() {
        BookRequest request = new BookRequest("Clean Code", "9780132350884", 2008, 1L, 2L, 464);
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        assertThatThrownBy(() -> bookService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("isbn");
        verify(bookRepository, never()).save(any());
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(bookRepository.findByIdWithRelations(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
