package com.curso.library.genre.application;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.library.book.domain.BookRepository;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.common.error.ResourceInUseException;
import com.curso.library.common.error.ResourceNotFoundException;
import com.curso.library.genre.api.dto.GenreRequest;
import com.curso.library.genre.api.dto.GenreResponse;
import com.curso.library.genre.domain.Genre;
import com.curso.library.genre.domain.GenreRepository;

@Service
@Transactional
public class GenreService {

    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;

    public GenreService(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<GenreResponse> findAll() {
        return genreRepository.findAll(Sort.by("name"))
                .stream()
                .map(genre -> GenreMapper.toResponse(genre, bookRepository.countByGenreId(genre.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public GenreResponse findById(Long id) {
        Genre genre = findGenre(id);
        return GenreMapper.toResponse(genre, bookRepository.countByGenreId(id));
    }

    public GenreResponse create(GenreRequest request) {
        ensureNameIsUnique(request.name(), null);
        Genre saved = genreRepository.save(GenreMapper.toEntity(request));
        return GenreMapper.toResponse(saved, 0);
    }

    public GenreResponse update(Long id, GenreRequest request) {
        Genre genre = findGenre(id);
        ensureNameIsUnique(request.name(), id);
        genre.update(request.name().trim(), blankToNull(request.description()));
        return GenreMapper.toResponse(genre, bookRepository.countByGenreId(id));
    }

    public void delete(Long id) {
        Genre genre = findGenre(id);
        if (bookRepository.countByGenreId(id) > 0) {
            throw new ResourceInUseException("Genre still has books on the shelf: " + genre.getName());
        }
        genreRepository.delete(genre);
    }

    private Genre findGenre(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
    }

    private void ensureNameIsUnique(String name, Long currentId) {
        boolean taken = currentId == null
                ? genreRepository.existsByNameIgnoreCase(name.trim())
                : genreRepository.existsByNameIgnoreCaseAndIdNot(name.trim(), currentId);
        if (taken) {
            throw new DuplicateResourceException("name", name);
        }
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
