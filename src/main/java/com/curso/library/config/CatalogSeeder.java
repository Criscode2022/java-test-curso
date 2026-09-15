package com.curso.library.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.curso.library.book.domain.Book;
import com.curso.library.book.domain.BookRepository;

@Component
class CatalogSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;

    CatalogSeeder(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        if (bookRepository.count() > 0) {
            return;
        }

        bookRepository.saveAll(List.of(
                new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008),
                new Book("Effective Java", "Joshua Bloch", "9780134685991", 2018)
        ));
    }
}
