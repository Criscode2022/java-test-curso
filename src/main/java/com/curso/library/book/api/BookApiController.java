package com.curso.library.book.api;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.curso.library.auth.application.CurrentUserService;
import com.curso.library.book.api.dto.BookRequest;
import com.curso.library.book.api.dto.BookResponse;
import com.curso.library.book.application.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookApiController {

    private final BookService bookService;
    private final CurrentUserService currentUserService;

    public BookApiController(BookService bookService, CurrentUserService currentUserService) {
        this.bookService = bookService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public List<BookResponse> findAll(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Long ownerId
    ) {
        if (authorId != null) {
            return bookService.findByAuthor(authorId);
        }
        if (genreId != null) {
            return bookService.findByGenre(genreId);
        }
        if (ownerId != null) {
            return bookService.findByOwner(ownerId);
        }
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookResponse findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@Valid @RequestBody BookRequest request) {
        BookResponse created = bookService.create(request, currentUserService.requireUser());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.update(id, request, currentUserService.requireUser());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.delete(id, currentUserService.requireUser());
    }
}
