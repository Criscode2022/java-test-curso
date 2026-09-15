package com.curso.library.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.curso.library.author.application.AuthorService;
import com.curso.library.book.application.BookService;
import com.curso.library.genre.application.GenreService;

@Controller
public class HomeController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;

    public HomeController(BookService bookService, AuthorService authorService, GenreService genreService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var books = bookService.findAll();
        model.addAttribute("bookCount", books.size());
        model.addAttribute("authorCount", authorService.findAll().size());
        model.addAttribute("genreCount", genreService.findAll().size());
        model.addAttribute("books", books);
        return "home";
    }
}
