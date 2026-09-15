package com.curso.library.book.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookService(
            BookRepository bookRepository,
            AuthorRepository authorRepository,
            GenreRepository genreRepository
    ) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAllWithRelations()
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByAuthor(Long authorId) {
        findAuthor(authorId);
        return bookRepository.findByAuthorId(authorId)
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findByGenre(Long genreId) {
        findGenre(genreId);
        return bookRepository.findByGenreId(genreId)
                .stream()
                .map(BookMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return BookMapper.toResponse(findBook(id));
    }

    public BookResponse create(BookRequest request) {
        ensureIsbnIsUnique(request.isbn(), null);
        Book saved = bookRepository.save(
                BookMapper.toEntity(request, findAuthor(request.authorId()), findGenre(request.genreId()))
        );
        return BookMapper.toResponse(saved);
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = findBook(id);
        ensureIsbnIsUnique(request.isbn(), id);
        book.update(
                request.title().trim(),
                request.isbn().trim(),
                request.publishedYear(),
                request.pages(),
                findAuthor(request.authorId()),
                findGenre(request.genreId())
        );
        return BookMapper.toResponse(book);
    }

    public void delete(Long id) {
        bookRepository.delete(findBook(id));
    }

    private Book findBook(Long id) {
        return bookRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    private Genre findGenre(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
    }

    private void ensureIsbnIsUnique(String isbn, Long currentId) {
        boolean taken = currentId == null
                ? bookRepository.existsByIsbn(isbn)
                : bookRepository.existsByIsbnAndIdNot(isbn, currentId);
        if (taken) {
            throw new DuplicateResourceException("isbn", isbn);
        }
    }
}
