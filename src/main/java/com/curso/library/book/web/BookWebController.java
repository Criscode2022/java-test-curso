package com.curso.library.book.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.library.auth.application.CurrentUserService;
import com.curso.library.author.application.AuthorService;
import com.curso.library.book.api.dto.BookRequest;
import com.curso.library.book.application.BookService;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.common.error.ResourceNotFoundException;
import com.curso.library.genre.application.GenreService;
import com.curso.library.user.domain.User;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookWebController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final CurrentUserService currentUserService;

    public BookWebController(
            BookService bookService,
            AuthorService authorService,
            GenreService genreService,
            CurrentUserService currentUserService
    ) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("book", BookRequest.empty());
        model.addAttribute("formTitle", "Add a book");
        model.addAttribute("formAction", "/books");
        addLookups(model);
        return "books/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("book") BookRequest book,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return showForm(model, "Add a book", "/books");
        }

        try {
            bookService.create(book, currentUser());
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("isbn", "duplicate", exception.getMessage());
            return showForm(model, "Add a book", "/books");
        } catch (ResourceNotFoundException exception) {
            rejectMissingRelation(bindingResult, exception);
            return showForm(model, "Add a book", "/books");
        }

        redirectAttributes.addFlashAttribute("message", "Book saved.");
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "books/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        bookService.ensureOwner(id, currentUser());
        model.addAttribute("book", BookRequest.from(bookService.findById(id)));
        model.addAttribute("formTitle", "Edit book");
        model.addAttribute("formAction", "/books/" + id);
        addLookups(model);
        return "books/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("book") BookRequest book,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return showForm(model, "Edit book", "/books/" + id);
        }

        try {
            bookService.update(id, book, currentUser());
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("isbn", "duplicate", exception.getMessage());
            return showForm(model, "Edit book", "/books/" + id);
        } catch (ResourceNotFoundException exception) {
            rejectMissingRelation(bindingResult, exception);
            return showForm(model, "Edit book", "/books/" + id);
        }

        redirectAttributes.addFlashAttribute("message", "Book updated.");
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        bookService.delete(id, currentUser());
        redirectAttributes.addFlashAttribute("message", "Book deleted.");
        return "redirect:/books";
    }

    private User currentUser() {
        return currentUserService.requireUser();
    }

    private String showForm(Model model, String title, String action) {
        model.addAttribute("formTitle", title);
        model.addAttribute("formAction", action);
        addLookups(model);
        return "books/form";
    }

    private void addLookups(Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
    }

    private static void rejectMissingRelation(BindingResult bindingResult, ResourceNotFoundException exception) {
        String field = exception.getMessage().startsWith("Genre") ? "genreId" : "authorId";
        bindingResult.rejectValue(field, "missing", exception.getMessage());
    }
}
