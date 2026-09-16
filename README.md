# Stacks

A Spring Boot library with real **Author → Book**, **Genre → Book**, and **User → Book (owner)** foreign keys, JWT auth, JSON APIs, and a Thymeleaf UI.

```
authors 1 ──< books >── 1 genres
users   1 ──< books
```

You cannot delete an author or a genre that still has books. Only a book's owner can edit or delete it.

Auth is a local `library_users` table plus JWT. This is not Neon Auth.

## Run

Copy `.env.example` to `.env` if needed, then:

```bash
.\mvnw.cmd spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080).

Demo account: `librarian@stacks.local` / `stacks`

## Model

| Table | Notes |
|-------|--------|
| `library_users` | email, bcrypt password, name |
| `authors` | name, nationality, birth year |
| `genres` | name, description |
| `books` | title, isbn, year, pages, `author_id` FK, `genre_id` FK, `owner_id` FK |

Schema changes go in `src/main/resources/db/migration`. Flyway owns the database. Hibernate only validates.

## Auth

- `POST /api/auth/register` and `POST /api/auth/login` return a JWT
- Send `Authorization: Bearer <token>` on writes
- The web login form stores the same JWT in an HttpOnly cookie
- Catalog GET pages stay public; adding or changing data needs a signed-in user

## Pages

- `/` home with counts and a shelf
- `/books` search, author filter, genre filter
- `/authors` people and book counts
- `/genres` shelves by kind
- `/login` and `/register`
- `/api/books`, `/api/authors`, `/api/genres`, `/api/auth`

## Tests

```bash
.\mvnw.cmd test
```

Tests use H2. The running app uses Neon.
