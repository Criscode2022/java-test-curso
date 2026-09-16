package com.curso.library.auth.application;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.curso.library.user.domain.User;
import com.curso.library.user.domain.UserRepository;

@Service
@Transactional(readOnly = true)
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser() {
        return findUser().orElseThrow(() -> new IllegalStateException("Authenticated request without a user"));
    }

    public Optional<User> findUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof AuthPrincipal principal) {
            return userRepository.findById(principal.id());
        }

        return userRepository.findByEmailIgnoreCase(authentication.getName());
    }
}
