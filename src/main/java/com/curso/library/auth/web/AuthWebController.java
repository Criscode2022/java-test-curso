package com.curso.library.auth.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.curso.library.auth.api.dto.LoginRequest;
import com.curso.library.auth.api.dto.RegisterRequest;
import com.curso.library.auth.api.dto.TokenResponse;
import com.curso.library.auth.application.AuthService;
import com.curso.library.auth.application.CurrentUserService;
import com.curso.library.auth.application.InvalidCredentialsException;
import com.curso.library.auth.application.JwtProperties;
import com.curso.library.auth.security.AuthCookie;
import com.curso.library.common.error.DuplicateResourceException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class AuthWebController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;
    private final JwtProperties jwtProperties;

    public AuthWebController(
            AuthService authService,
            CurrentUserService currentUserService,
            JwtProperties jwtProperties
    ) {
        this.authService = authService;
        this.currentUserService = currentUserService;
        this.jwtProperties = jwtProperties;
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        if (currentUserService.findUser().isPresent()) {
            return "redirect:/";
        }
        model.addAttribute("login", LoginRequest.empty());
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(
            @Valid @ModelAttribute("login") LoginRequest login,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            TokenResponse token = authService.login(login);
            AuthCookie.write(response, token.token(), jwtProperties);
        } catch (InvalidCredentialsException exception) {
            bindingResult.reject("badCredentials", exception.getMessage());
            return "auth/login";
        }

        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        if (currentUserService.findUser().isPresent()) {
            return "redirect:/";
        }
        model.addAttribute("register", RegisterRequest.empty());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("register") RegisterRequest register,
            BindingResult bindingResult,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            TokenResponse token = authService.register(register);
            AuthCookie.write(response, token.token(), jwtProperties);
        } catch (DuplicateResourceException exception) {
            bindingResult.rejectValue("email", "duplicate", exception.getMessage());
            return "auth/register";
        }

        redirectAttributes.addFlashAttribute("message", "Welcome to Stacks.");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AuthCookie.clear(response, jwtProperties);
        redirectAttributes.addFlashAttribute("message", "Signed out.");
        return "redirect:/";
    }
}
