package com.curso.library.common.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.curso.library.author.application.AuthorService;
import com.curso.library.book.application.BookService;

@Controller
public class HomeController {

    private final BookService bookService;
    private final AuthorService authorService;

    public HomeController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @GetMapping("/")
    public String home(Model model) {
        var books = bookService.findAll();
        var authors = authorService.findAll();
        model.addAttribute("bookCount", books.size());
        model.addAttribute("authorCount", authors.size());
        model.addAttribute("books", books);
        model.addAttribute("authors", authors);
        return "home";
    }
}
