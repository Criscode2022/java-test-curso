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

    @Query("select b from Book b join fetch b.author order by b.title")
    List<Book> findAllWithAuthor();

    @Query("select b from Book b join fetch b.author where b.id = :id")
    Optional<Book> findByIdWithAuthor(@Param("id") Long id);

    @Query("select b from Book b join fetch b.author where b.author.id = :authorId order by b.title")
    List<Book> findByAuthorId(@Param("authorId") Long authorId);
}
