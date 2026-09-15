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

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponse> findAll() {
        return bookRepository.findAllWithAuthor()
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
    public BookResponse findById(Long id) {
        return BookMapper.toResponse(findBook(id));
    }

    public BookResponse create(BookRequest request) {
        ensureIsbnIsUnique(request.isbn(), null);
        Book saved = bookRepository.save(BookMapper.toEntity(request, findAuthor(request.authorId())));
        return BookMapper.toResponse(saved);
    }

    public BookResponse update(Long id, BookRequest request) {
        Book book = findBook(id);
        ensureIsbnIsUnique(request.isbn(), id);
        book.update(
                request.title().trim(),
                request.isbn().trim(),
                request.publishedYear(),
                request.genre() == null || request.genre().isBlank() ? null : request.genre().trim(),
                request.pages(),
                findAuthor(request.authorId())
        );
        return BookMapper.toResponse(book);
    }

    public void delete(Long id) {
        bookRepository.delete(findBook(id));
    }

    private Book findBook(Long id) {
        return bookRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", id));
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
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
