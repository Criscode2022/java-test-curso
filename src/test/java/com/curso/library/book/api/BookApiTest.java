package com.curso.library.book.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crudFlowWorks() throws Exception {
        String token = librarianToken();

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        mockMvc.perform(get("/api/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        String body = """
                {
                  "title": "The Pragmatic Programmer",
                  "isbn": "9780201616224",
                  "publishedYear": 1999,
                  "authorId": 1,
                  "genreId": 2,
                  "pages": 352
                }
                """;

        String location = mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("The Pragmatic Programmer"))
                .andExpect(jsonPath("$.ownerName").value("Librarian"))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780201616224"));

        String updated = """
                {
                  "title": "The Pragmatic Programmer",
                  "isbn": "9780201616224",
                  "publishedYear": 1999,
                  "authorId": 1,
                  "genreId": 2,
                  "pages": 352
                }
                """;

        mockMvc.perform(put(location)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genreName").value("Software"));

        mockMvc.perform(delete(location)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRejectsAnonymous() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createRejectsInvalidPayload() throws Exception {
        String invalid = """
                {
                  "title": "",
                  "isbn": "123",
                  "publishedYear": 1200
                }
                """;

        mockMvc.perform(post("/api/books")
                        .header("Authorization", "Bearer " + librarianToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.title").exists())
                .andExpect(jsonPath("$.fieldErrors.isbn").exists())
                .andExpect(jsonPath("$.fieldErrors.authorId").exists())
                .andExpect(jsonPath("$.fieldErrors.genreId").exists());
    }

    @Test
    void cannotDeleteAuthorWithBooks() throws Exception {
        mockMvc.perform(delete("/api/authors/1")
                        .header("Authorization", "Bearer " + librarianToken()))
                .andExpect(status().isConflict());
    }

    @Test
    void cannotDeleteGenreWithBooks() throws Exception {
        mockMvc.perform(delete("/api/genres/2")
                        .header("Authorization", "Bearer " + librarianToken()))
                .andExpect(status().isConflict());
    }

    @Test
    void otherUserCannotDeleteOwnedBook() throws Exception {
        String register = """
                {
                  "name": "Ada",
                  "email": "ada@stacks.local",
                  "password": "stacks1"
                }
                """;

        String token = JsonPath.read(
                mockMvc.perform(post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(register))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                "$.token"
        );

        mockMvc.perform(delete("/api/books/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    private String librarianToken() throws Exception {
        String login = """
                {
                  "email": "librarian@stacks.local",
                  "password": "stacks"
                }
                """;

        String body = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(login))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(body, "$.token");
    }
}
