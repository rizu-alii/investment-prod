package com.invest.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void checkDatabaseConnection() {
        try {
            System.out.println("Testing database connection...");
            System.out.println("Database URL: " + env.getProperty("spring.datasource.url"));
            System.out.println("Database Username: " + env.getProperty("spring.datasource.username"));
            
            Connection connection = dataSource.getConnection();
            System.out.println("Database connection successful!");
            System.out.println("Database Product Name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("Database Product Version: " + connection.getMetaData().getDatabaseProductVersion());
            connection.close();
        } catch (Exception e) {
            System.err.println("Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }
} 