package com.upc.leadyourway.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/leadyourway/v1/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:8080", "http://leadyourway.up.railway.app")
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}

