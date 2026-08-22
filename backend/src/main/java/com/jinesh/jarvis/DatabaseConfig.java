package com.jinesh.jarvis;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String configuredUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() {

        String url = configuredUrl;

        // Render PostgreSQL connection string:
        // postgres://user:password@host:port/database
        if (url.startsWith("postgres://")) {
            url = "jdbc:postgresql://"
                    + url.substring("postgres://".length());
        }

        // Also support:
        // postgresql://user:password@host:port/database
        else if (url.startsWith("postgresql://")) {
            url = "jdbc:postgresql://"
                    + url.substring("postgresql://".length());
        }

        DriverManagerDataSource dataSource =
                new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }
}
