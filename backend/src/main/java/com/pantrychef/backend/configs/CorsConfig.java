package com.pantrychef.backend.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration for Cross Origin Resource Sharing
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * Applies the CORS policy
     * @param registry Essentially, the list of permitted origins
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173,http://0.0.0.0:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}