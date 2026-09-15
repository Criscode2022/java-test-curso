package com.curso.library.author.application;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.library.author.api.dto.AuthorRequest;
import com.curso.library.author.api.dto.AuthorResponse;
import com.curso.library.author.domain.Author;
import com.curso.library.author.domain.AuthorRepository;
import com.curso.library.book.domain.BookRepository;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.common.error.ResourceInUseException;
import com.curso.library.common.error.ResourceNotFoundException;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<AuthorResponse> findAll() {
        return authorRepository.findAll(Sort.by("name"))
                .stream()
                .map(author -> AuthorMapper.toResponse(author, bookRepository.countByAuthorId(author.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public AuthorResponse findById(Long id) {
        Author author = findAuthor(id);
        return AuthorMapper.toResponse(author, bookRepository.countByAuthorId(id));
    }

    public AuthorResponse create(AuthorRequest request) {
        ensureNameIsUnique(request.name(), null);
        Author saved = authorRepository.save(AuthorMapper.toEntity(request));
        return AuthorMapper.toResponse(saved, 0);
    }

    public AuthorResponse update(Long id, AuthorRequest request) {
        Author author = findAuthor(id);
        ensureNameIsUnique(request.name(), id);
        author.update(
                request.name().trim(),
                request.nationality() == null || request.nationality().isBlank() ? null : request.nationality().trim(),
                request.birthYear()
        );
        return AuthorMapper.toResponse(author, bookRepository.countByAuthorId(id));
    }

    public void delete(Long id) {
        Author author = findAuthor(id);
        if (bookRepository.countByAuthorId(id) > 0) {
            throw new ResourceInUseException("Author still has books on the shelf: " + author.getName());
        }
        authorRepository.delete(author);
    }

    private Author findAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    private void ensureNameIsUnique(String name, Long currentId) {
        boolean taken = currentId == null
                ? authorRepository.existsByNameIgnoreCase(name.trim())
                : authorRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), currentId);
        if (taken) {
            throw new DuplicateResourceException("name", name);
        }
    }
}
