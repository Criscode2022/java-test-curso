package com.curso.library.auth.application;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "library.jwt")
public record JwtProperties(
        String secret,
        Duration expiration,
        String cookieName
) {
}
