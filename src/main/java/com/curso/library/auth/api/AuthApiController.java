package com.curso.library.auth.api;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.curso.library.auth.api.dto.LoginRequest;
import com.curso.library.auth.api.dto.RegisterRequest;
import com.curso.library.auth.api.dto.TokenResponse;
import com.curso.library.auth.application.AuthService;
import com.curso.library.auth.application.CurrentUserService;
import com.curso.library.user.api.dto.UserResponse;
import com.curso.library.user.application.UserMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;
    private final CurrentUserService currentUserService;

    public AuthApiController(AuthService authService, CurrentUserService currentUserService) {
        this.authService = authService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        TokenResponse created = authService.register(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/auth/me")
                .build()
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me() {
        return UserMapper.toResponse(currentUserService.requireUser());
    }
}
