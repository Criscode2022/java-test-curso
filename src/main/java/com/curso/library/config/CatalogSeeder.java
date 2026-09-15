package com.curso.library.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.curso.library.author.domain.Author;
import com.curso.library.author.domain.AuthorRepository;
import com.curso.library.book.domain.Book;
import com.curso.library.book.domain.BookRepository;

@Component
class CatalogSeeder implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    CatalogSeeder(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        Author martin = findOrCreateAuthor("Robert C. Martin", "United States", 1952);
        Author bloch = findOrCreateAuthor("Joshua Bloch", "United States", 1961);
        Author fowler = findOrCreateAuthor("Martin Fowler", "United Kingdom", 1963);
        Author evans = findOrCreateAuthor("Eric Evans", "United States", null);

        addBookIfMissing("Clean Code", "9780132350884", 2008, "Software", 464, martin);
        addBookIfMissing("Clean Architecture", "9780134494166", 2017, "Software", 432, martin);
        addBookIfMissing("Effective Java", "9780134685991", 2018, "Software", 416, bloch);
        addBookIfMissing("Refactoring", "9780134757599", 2018, "Software", 448, fowler);
        addBookIfMissing("Domain-Driven Design", "9780321125217", 2003, "Architecture", 560, evans);
    }

    private Author findOrCreateAuthor(String name, String nationality, Integer birthYear) {
        return authorRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepository.save(new Author(name, nationality, birthYear)));
    }

    private void addBookIfMissing(
            String title,
            String isbn,
            Integer year,
            String genre,
            Integer pages,
            Author author
    ) {
        if (bookRepository.existsByIsbn(isbn)) {
            return;
        }
        bookRepository.save(new Book(title, isbn, year, genre, pages, author));
    }
}
