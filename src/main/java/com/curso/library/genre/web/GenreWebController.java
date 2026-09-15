package com.curso.library.genre.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.library.book.application.BookService;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.common.error.ResourceInUseException;
import com.curso.library.genre.api.dto.GenreRequest;
import com.curso.library.genre.application.GenreService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/genres")
public class GenreWebController {

    private final GenreService genreService;
    private final BookService bookService;

    public GenreWebController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("genres", genreService.findAll());
        return "genres/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("genre", GenreRequest.empty());
        model.addAttribute("formTitle", "Add a genre");
        model.addAttribute("formAction", "/genres");
        return "genres/form";
    }

    @PostMapping
    public String create(
            @Valid @ModelAttribute("genre") GenreRequest genre,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return showForm(model, "Add a genre", "/genres");
        }

        try {
            genreService.create(genre);
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("name", "duplicate", exception.getMessage());
            return showForm(model, "Add a genre", "/genres");
        }

        redirectAttributes.addFlashAttribute("message", "Genre saved.");
        return "redirect:/genres";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("genre", genreService.findById(id));
        model.addAttribute("books", bookService.findByGenre(id));
        return "genres/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("genre", GenreRequest.from(genreService.findById(id)));
        model.addAttribute("formTitle", "Edit genre");
        model.addAttribute("formAction", "/genres/" + id);
        return "genres/form";
    }

    @PostMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("genre") GenreRequest genre,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return showForm(model, "Edit genre", "/genres/" + id);
        }

        try {
            genreService.update(id, genre);
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("name", "duplicate", exception.getMessage());
            return showForm(model, "Edit genre", "/genres/" + id);
        }

        redirectAttributes.addFlashAttribute("message", "Genre updated.");
        return "redirect:/genres/" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            genreService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Genre deleted.");
        } catch (ResourceInUseException exception) {
            redirectAttributes.addFlashAttribute("message", exception.getMessage());
        }
        return "redirect:/genres";
    }

    private static String showForm(Model model, String title, String action) {
        model.addAttribute("formTitle", title);
        model.addAttribute("formAction", action);
        return "genres/form";
    }
}
