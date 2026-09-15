# Stacks

A Spring Boot library with a real **Author → Book** foreign key, JSON APIs, and a Thymeleaf UI that looks like books on a shelf.

```
authors 1 ──< books
```

You cannot delete an author who still has books.

## Run

Copy `.env.example` to `.env` if needed, then:

```bash
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

## Model

| Table | Notes |
|-------|--------|
| `authors` | name, nationality, birth year |
| `books` | title, isbn, year, genre, pages, `author_id` FK |

Schema changes go in `src/main/resources/db/migration`. Flyway owns the database. Hibernate only validates.

## Pages

- `/` home with counts and a shelf
- `/books` search and author filter
- `/authors` people and book counts
- `/api/books` and `/api/authors` JSON

## Tests

```bash
.\mvnw.cmd test
```

Tests use H2. The running app uses Neon.
