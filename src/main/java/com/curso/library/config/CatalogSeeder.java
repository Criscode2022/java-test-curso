package com.curso.library.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.curso.library.author.domain.Author;
import com.curso.library.author.domain.AuthorRepository;
import com.curso.library.book.domain.Book;
import com.curso.library.book.domain.BookRepository;
import com.curso.library.genre.domain.Genre;
import com.curso.library.genre.domain.GenreRepository;
import com.curso.library.user.domain.User;
import com.curso.library.user.domain.UserRepository;

@Component
class CatalogSeeder implements CommandLineRunner {

    static final String LIBRARIAN_EMAIL = "librarian@stacks.local";

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    CatalogSeeder(
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            BookRepository bookRepository,
            UserRepository userRepository
    ) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        User librarian = userRepository.findByEmailIgnoreCase(LIBRARIAN_EMAIL)
                .orElseThrow(() -> new IllegalStateException("Flyway V4 must create " + LIBRARIAN_EMAIL));

        Genre software = findOrCreateGenre("Software", "Programming and craft.");
        Genre architecture = findOrCreateGenre("Architecture", "How systems are shaped.");

        Author martin = findOrCreateAuthor("Robert C. Martin", "United States", 1952);
        Author bloch = findOrCreateAuthor("Joshua Bloch", "United States", 1961);
        Author fowler = findOrCreateAuthor("Martin Fowler", "United Kingdom", 1963);
        Author evans = findOrCreateAuthor("Eric Evans", "United States", null);

        addBookIfMissing("Clean Code", "9780132350884", 2008, 464, martin, software, librarian);
        addBookIfMissing("Clean Architecture", "9780134494166", 2017, 432, martin, software, librarian);
        addBookIfMissing("Effective Java", "9780134685991", 2018, 416, bloch, software, librarian);
        addBookIfMissing("Refactoring", "9780134757599", 2018, 448, fowler, software, librarian);
        addBookIfMissing("Domain-Driven Design", "9780321125217", 2003, 560, evans, architecture, librarian);
    }

    private Author findOrCreateAuthor(String name, String nationality, Integer birthYear) {
        return authorRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> authorRepository.save(new Author(name, nationality, birthYear)));
    }

    private Genre findOrCreateGenre(String name, String description) {
        return genreRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> genreRepository.save(new Genre(name, description)));
    }

    private void addBookIfMissing(
            String title,
            String isbn,
            Integer year,
            Integer pages,
            Author author,
            Genre genre,
            User owner
    ) {
        if (bookRepository.existsByIsbn(isbn)) {
            return;
        }
        bookRepository.save(new Book(title, isbn, year, pages, author, genre, owner));
    }
}
