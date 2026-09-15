package com.curso.library.author.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.library.author.api.dto.AuthorRequest;
import com.curso.library.author.application.AuthorService;
import com.curso.library.book.application.BookService;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.common.error.ResourceInUseException;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/authors")
public class AuthorWebController {

    private final AuthorService authorService;
    private final BookService bookService;

    public AuthorWebController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("authors", authorService.findAll());
        return "authors/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("author", AuthorRequest.empty());
        model.addAttribute("formTitle", "Add an author");
        model.addAttribute("formAction", "/authors");
        return "authors/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("author") AuthorRequest author,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return showForm(model, "Add an author", "/authors");
        }

        try {
            authorService.create(author);
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("name", "duplicate", exception.getMessage());
            return showForm(model, "Add an author", "/authors");
        }

        redirectAttributes.addFlashAttribute("message", "Author saved.");
        return "redirect:/authors";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("author", authorService.findById(id));
        model.addAttribute("books", bookService.findByAuthor(id));
        return "authors/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("author", AuthorRequest.from(authorService.findById(id)));
        model.addAttribute("formTitle", "Edit author");
        model.addAttribute("formAction", "/authors/" + id);
        return "authors/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("author") AuthorRequest author,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return showForm(model, "Edit author", "/authors/" + id);
        }

        try {
            authorService.update(id, author);
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("name", "duplicate", exception.getMessage());
            return showForm(model, "Edit author", "/authors/" + id);
        }

        redirectAttributes.addFlashAttribute("message", "Author updated.");
        return "redirect:/authors/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            authorService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Author removed.");
        } catch (ResourceInUseException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/authors";
    }

    private static String showForm(Model model, String title, String action) {
        model.addAttribute("formTitle", title);
        model.addAttribute("formAction", action);
        return "authors/form";
    }
}
