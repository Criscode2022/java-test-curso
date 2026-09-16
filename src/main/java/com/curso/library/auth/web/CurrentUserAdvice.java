package com.curso.library.auth.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.curso.library.auth.application.CurrentUserService;
import com.curso.library.user.api.dto.UserResponse;
import com.curso.library.user.application.UserMapper;

@ControllerAdvice
public class CurrentUserAdvice {

    private final CurrentUserService currentUserService;

    public CurrentUserAdvice(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @ModelAttribute("currentUser")
    public UserResponse currentUser() {
        return currentUserService.findUser()
                .map(UserMapper::toResponse)
                .orElse(null);
    }
}
