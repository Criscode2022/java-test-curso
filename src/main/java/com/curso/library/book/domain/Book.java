package com.curso.library.book.domain;

import com.curso.library.author.domain.Author;
import com.curso.library.genre.domain.Genre;
import com.curso.library.user.domain.User;

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

    private Integer pages;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_books_author"))
    private Author author;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false, foreignKey = @ForeignKey(name = "fk_books_genre"))
    private Genre genre;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_books_owner"))
    private User owner;

    protected Book() {
    }

    public Book(String title, String isbn, Integer publishedYear, Integer pages, Author author, Genre genre, User owner) {
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.pages = pages;
        this.author = author;
        this.genre = genre;
        this.owner = owner;
    }

    public void update(String title, String isbn, Integer publishedYear, Integer pages, Author author, Genre genre) {
        this.title = title;
        this.isbn = isbn;
        this.publishedYear = publishedYear;
        this.pages = pages;
        this.author = author;
        this.genre = genre;
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

    public Integer getPages() {
        return pages;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public User getOwner() {
        return owner;
    }
}
