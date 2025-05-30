package com.invest.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void checkDatabaseConnection() {
        try {
            System.out.println("=== Database Connection Test ===");
            System.out.println("Database URL: " + env.getProperty("spring.datasource.url"));
            System.out.println("Database Username: " + env.getProperty("spring.datasource.username"));
            
            // Print HikariCP configuration
            System.out.println("\nHikariCP Configuration:");
            System.out.println("Maximum Pool Size: " + env.getProperty("spring.datasource.hikari.maximum-pool-size"));
            System.out.println("Minimum Idle: " + env.getProperty("spring.datasource.hikari.minimum-idle"));
            System.out.println("Connection Timeout: " + env.getProperty("spring.datasource.hikari.connection-timeout"));
            
            Connection connection = dataSource.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            
            System.out.println("\nConnection Successful!");
            System.out.println("Database Product Name: " + metaData.getDatabaseProductName());
            System.out.println("Database Product Version: " + metaData.getDatabaseProductVersion());
            System.out.println("Driver Name: " + metaData.getDriverName());
            System.out.println("Driver Version: " + metaData.getDriverVersion());
            System.out.println("URL: " + metaData.getURL());
            System.out.println("Username: " + metaData.getUserName());
            
            // Test query
            try (var stmt = connection.createStatement()) {
                var rs = stmt.executeQuery("SELECT version()");
                if (rs.next()) {
                    System.out.println("PostgreSQL Version: " + rs.getString(1));
                }
            }
            
            connection.close();
            System.out.println("=== Connection Test Complete ===");
        } catch (Exception e) {
            System.err.println("\n=== Database Connection Failed! ===");
            System.err.println("Error Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("\nStack Trace:");
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }
} 