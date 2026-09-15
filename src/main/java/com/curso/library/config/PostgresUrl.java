package com.curso.library.config;

import java.net.URI;
import java.net.URISyntaxException;

record PostgresUrl(String jdbcUrl, String username, String password) {

    static PostgresUrl parse(String databaseUrl) {
        if (databaseUrl == null || databaseUrl.isBlank()) {
            throw new IllegalStateException("DATABASE_URL is missing. Copy .env.example to .env.");
        }

        try {
            String raw = databaseUrl.trim();
            if (raw.startsWith("jdbc:")) {
                raw = raw.substring("jdbc:".length());
            }
            if (raw.startsWith("postgres://")) {
                raw = "postgresql://" + raw.substring("postgres://".length());
            }
            if (!raw.startsWith("postgresql://")) {
                throw new IllegalStateException("DATABASE_URL must be a PostgreSQL URL");
            }

            URI uri = new URI(raw);
            String userInfo = uri.getUserInfo();
            String username = "";
            String password = "";
            if (userInfo != null) {
                int colon = userInfo.indexOf(':');
                username = colon < 0 ? userInfo : userInfo.substring(0, colon);
                password = colon < 0 ? "" : userInfo.substring(colon + 1);
            }

            String query = uri.getQuery() == null ? "" : uri.getQuery();
            query = query.replaceAll("(?i)channel_binding=[^&]*", "").replaceAll("&&", "&");
            query = query.replaceAll("^&|&$", "");
            if (!query.toLowerCase().contains("sslmode=")) {
                query = query.isBlank() ? "sslmode=require" : query + "&sslmode=require";
            }

            int port = uri.getPort();
            String host = port == -1 ? uri.getHost() : uri.getHost() + ":" + port;
            String jdbcUrl = "jdbc:postgresql://" + host + uri.getPath() + "?" + query;
            return new PostgresUrl(jdbcUrl, username, password);
        } catch (URISyntaxException exception) {
            throw new IllegalStateException("DATABASE_URL is not a valid URI", exception);
        }
    }

    static String toDirectHost(String pooledJdbcUrl) {
        return pooledJdbcUrl.replace("-pooler.", ".");
    }
}
