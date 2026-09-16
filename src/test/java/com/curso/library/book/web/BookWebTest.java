package com.curso.library.book.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listPageRendersCatalog() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(content().string(containsString("Clean Code")))
                .andExpect(content().string(containsString("Effective Java")));
    }

    @Test
    void homeShowsLobby() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Your library")));
    }

    @Test
    void authorsPageRenders() throws Exception {
        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Robert C. Martin")));
    }

    @Test
    void genresPageRenders() throws Exception {
        mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Software")));
    }

    @Test
    void createFormRequiresLogin() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void createFormRedisplaysValidationErrors() throws Exception {
        mockMvc.perform(post("/books")
                        .with(user("librarian@stacks.local"))
                        .with(csrf())
                        .param("title", "")
                        .param("isbn", "123")
                        .param("publishedYear", "1200"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("title is required")))
                .andExpect(content().string(containsString("isbn must be between 10 and 17 characters")));
    }

    @Test
    void createBookRedirectsToCatalog() throws Exception {
        mockMvc.perform(post("/books")
                        .with(user("librarian@stacks.local"))
                        .with(csrf())
                        .param("title", "The Pragmatic Programmer")
                        .param("isbn", "9780201616224")
                        .param("publishedYear", "1999")
                        .param("authorId", "1")
                        .param("genreId", "2")
                        .param("pages", "352"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void missingBookRendersNotFoundPage() throws Exception {
        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not on these shelves")));
    }
}
