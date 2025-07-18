package com.practicalTest.practicalTest.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    public DotenvConfig() {
        Dotenv dotenv = Dotenv.configure()
                .directory("./practicalTest") // Specify the directory containing .env
                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}