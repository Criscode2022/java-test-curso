package com.curso.library.auth.application;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.curso.library.user.domain.User;

public record AuthPrincipal(
        Long id,
        String email,
        String name,
        String password
) implements UserDetails {

    public static AuthPrincipal from(User user) {
        return new AuthPrincipal(user.getId(), user.getEmail(), user.getName(), user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
