package com.curso.library.auth.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.library.auth.api.dto.LoginRequest;
import com.curso.library.auth.api.dto.RegisterRequest;
import com.curso.library.auth.api.dto.TokenResponse;
import com.curso.library.common.error.DuplicateResourceException;
import com.curso.library.user.application.UserMapper;
import com.curso.library.user.domain.User;
import com.curso.library.user.domain.UserRepository;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            JwtProperties jwtProperties
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public TokenResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("email", email);
        }

        User saved = userRepository.save(new User(
                email,
                passwordEncoder.encode(request.password()),
                request.name().trim()
        ));
        return tokenFor(saved);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        return tokenFor(user);
    }

    private TokenResponse tokenFor(User user) {
        return new TokenResponse(
                jwtService.createToken(user),
                "Bearer",
                jwtProperties.expiration().toSeconds(),
                UserMapper.toResponse(user)
        );
    }

}
