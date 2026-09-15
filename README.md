# Library

A Spring Boot Book CRUD for students: JSON API, Thymeleaf pages, and Neon Postgres.

```
Browser  -> book.web.BookWebController  -> templates/
JSON     -> book.api.BookApiController  -> BookRequest / BookResponse
                         \              /
                    book.application.BookService
                               |
                       book.domain.BookRepository
                               |
                         Neon (Flyway schema)
```

## Requirements

- Java 21
- A `.env` file (copy `.env.example`)

## Database

The app uses a Neon project named **java-test-curso** (database `library`).

1. Copy `.env.example` to `.env`
2. Paste the pooled URL as `DATABASE_URL`
3. Paste the direct URL as `DATABASE_URL_UNPOOLED` (Flyway uses this)

`.env` is gitignored. Never commit the password.

Schema lives in `src/main/resources/db/migration`. Flyway runs it on startup. Hibernate only **validates** the schema.

Tests use H2 (`application-test.yml`) so `mvn test` does not need Neon.

## Run

```bash
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080/books](http://localhost:8080/books).

## Package map

```
com.curso.library
  LibraryApplication.java
  book/
    domain/          Book, BookRepository
    application/     BookService, BookMapper
    api/             BookApiController + dto/
    web/             BookWebController
  common/
    error/           400 / 404 / 409 handlers
    web/             HomeController
  config/            .env loader, Neon URL parser, catalog seeder
```

## Pages and API

| Method | Path | Result |
|--------|------|--------|
| GET | `/books` | catalog page |
| GET | `/api/books` | JSON list |
| POST | `/api/books` | 201 / 400 / 409 |

More examples: `requests.http`.

## Tests

```bash
.\mvnw.cmd test
```
