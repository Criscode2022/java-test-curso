package com.curso.library.book.domain;

import com.curso.library.author.domain.Author;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 17)
    private String isbn;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(length = 60)
    private String genre;

    private Integer pages;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_books_author"))
    private Author author;

    protected Book() {
    }

    public Book(String title, String isbn, Integer publishedYear, String genre, Integer pages, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.pages = pages;
        this.author = author;
    }

    public void update(String title, String isbn, Integer publishedYear, String genre, Integer pages, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.genre = genre;
        this.pages = pages;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getPublishedYear() {
        return publishedYear;
    }

    public String getGenre() {
        return genre;
    }

    public Integer getPages() {
        return pages;
    }

    public Author getAuthor() {
        return author;
    }
}
