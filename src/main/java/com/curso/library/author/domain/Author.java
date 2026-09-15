package com.curso.library.author.domain;

import java.util.ArrayList;
import java.util.List;

import com.curso.library.book.domain.Book;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(length = 80)
    private String nationality;

    @Column(name = "birth_year")
    private Integer birthYear;

    @OneToMany(mappedBy = "author")
    private List<Book> books = new ArrayList<>();

    protected Author() {
    }

    public Author(String name, String nationality, Integer birthYear) {
        this.name = name;
        this.nationality = nationality;
        this.birthYear = birthYear;
    }

    public void update(String name, String nationality, Integer birthYear) {
        this.name = name;
        this.nationality = nationality;
        this.birthYear = birthYear;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNationality() {
        return nationality;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public List<Book> getBooks() {
        return books;
    }
}
