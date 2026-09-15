# Stacks

A Spring Boot library with real **Author → Book** and **Genre → Book** foreign keys, JSON APIs, and a Thymeleaf UI that looks like books on a shelf.

```
authors 1 ──< books >── 1 genres
```

You cannot delete an author or a genre that still has books.

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
| `genres` | name, description |
| `books` | title, isbn, year, pages, `author_id` FK, `genre_id` FK |

Schema changes go in `src/main/resources/db/migration`. Flyway owns the database. Hibernate only validates.

## Pages

- `/` home with counts and a shelf
- `/books` search, author filter, genre filter
- `/authors` people and book counts
- `/genres` shelves by kind
- `/api/books`, `/api/authors`, and `/api/genres` JSON

## Tests

```bash
.\mvnw.cmd test
```

Tests use H2. The running app uses Neon.
