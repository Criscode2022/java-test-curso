package com.curso.library.book.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    long countByAuthorId(Long authorId);

    long countByGenreId(Long genreId);

    @Query("select b from Book b join fetch b.author join fetch b.genre order by b.title")
    List<Book> findAllWithRelations();

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.id = :id")
    Optional<Book> findByIdWithRelations(@Param("id") Long id);

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.author.id = :authorId order by b.title")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.genre.id = :genreId order by b.title")
    List<Book> findByGenreId(@Param("genreId") Long genreId);
}
