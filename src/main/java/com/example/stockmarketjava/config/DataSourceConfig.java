package com.example.stockmarketjava.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/stockmarket");
        dataSource.setUsername("postgres");
        dataSource.setPassword("072");
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

}