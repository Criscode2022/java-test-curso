package com.curso.library.config;

import javax.sql.DataSource;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.flyway.autoconfigure.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@Profile("!test")
class NeonDataSourceConfiguration {

    @Bean
    DataSource dataSource(@Value("${DATABASE_URL}") String databaseUrl) {
        PostgresUrl parsed = PostgresUrl.parse(databaseUrl);
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(parsed.jdbcUrl());
        dataSource.setUsername(parsed.username());
        dataSource.setPassword(parsed.password());
        dataSource.setMaximumPoolSize(5);
        return dataSource;
    }

    @Bean
    FlywayConfigurationCustomizer flywayUsesDirectConnection(
            @Value("${DATABASE_URL_UNPOOLED:${DATABASE_URL}}") String unpooledUrl
    ) {
        return (FluentConfiguration configuration) -> {
            PostgresUrl parsed = PostgresUrl.parse(unpooledUrl);
            configuration.dataSource(parsed.jdbcUrl(), parsed.username(), parsed.password());
        };
    }
}
