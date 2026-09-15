package com.curso.library.book.web;

import static org.hamcrest.Matchers.containsString;
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
    void homeRedirectsToCatalog() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void createFormRedisplaysValidationErrors() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "")
                        .param("author", "Unknown")
                        .param("isbn", "123")
                        .param("publishedYear", "1200"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("title is required")))
                .andExpect(content().string(containsString("isbn must be between 10 and 17 characters")));
    }

    @Test
    void createBookRedirectsToCatalog() throws Exception {
        mockMvc.perform(post("/books")
                        .param("title", "The Pragmatic Programmer")
                        .param("author", "Andrew Hunt")
                        .param("isbn", "9780201616224")
                        .param("publishedYear", "1999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void missingBookRendersNotFoundPage() throws Exception {
        mockMvc.perform(get("/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("This book is not on the shelf")));
    }
}
