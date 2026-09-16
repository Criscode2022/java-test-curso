package com.curso.library.common.error;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.library.author.web.AuthorWebController;
import com.curso.library.book.web.BookWebController;
import com.curso.library.genre.web.GenreWebController;

@ControllerAdvice(assignableTypes = {BookWebController.class, AuthorWebController.class, GenreWebController.class})
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(ResourceNotFoundException exception, Model model) {
        model.addAttribute("message", exception.getMessage());
        return "error/not-found";
    }

    @ExceptionHandler(ForbiddenException.class)
    public String handleForbidden(ForbiddenException exception, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", exception.getMessage());
        return "redirect:/books";
    }
}
