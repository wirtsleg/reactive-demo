package com.example.reactivedemo.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.api.PostgresqlConnection;
import io.r2dbc.postgresql.api.PostgresqlResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresConfig {

    @Bean
    public PostgresqlConnection postgresqlConnection(
            @Value("${spring.r2dbc.username}") String username,
            @Value("${spring.r2dbc.password}") String password) {
        PostgresqlConnectionFactory factory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host("localhost")
                .port(5432)
                .username(username)
                .password(password)
                .database("reactive")
                .build());

        PostgresqlConnection connection = factory.create().block();

        connection.createStatement("LISTEN post_notification").execute()
                .flatMap(PostgresqlResult::getRowsUpdated)
                .subscribe();

        return connection;
    }
}
