package com.curso.library.author.api;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.curso.library.author.api.dto.AuthorRequest;
import com.curso.library.author.api.dto.AuthorResponse;
import com.curso.library.author.application.AuthorService;
import com.curso.library.book.api.dto.BookResponse;
import com.curso.library.book.application.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authors")
public class AuthorApiController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorApiController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public List<AuthorResponse> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public AuthorResponse findById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @GetMapping("/{id}/books")
    public List<BookResponse> findBooks(@PathVariable Long id) {
        return bookService.findByAuthor(id);
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@Valid @RequestBody AuthorRequest request) {
        AuthorResponse created = authorService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public AuthorResponse update(@PathVariable Long id, @Valid @RequestBody AuthorRequest request) {
        return authorService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        authorService.delete(id);
    }
}
