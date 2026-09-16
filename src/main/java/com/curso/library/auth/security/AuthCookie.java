package com.curso.library.auth.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import com.curso.library.auth.application.JwtProperties;

import jakarta.servlet.http.HttpServletResponse;

public final class AuthCookie {

    private AuthCookie() {
    }

    public static void write(HttpServletResponse response, String token, JwtProperties properties) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie(properties, token, properties.expiration()).toString());
    }

    public static void clear(HttpServletResponse response, JwtProperties properties) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookie(properties, "", java.time.Duration.ZERO).toString());
    }

    private static ResponseCookie cookie(JwtProperties properties, String token, java.time.Duration maxAge) {
        return ResponseCookie.from(properties.cookieName(), token)
                .httpOnly(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Lax")
                .build();
    }
}
