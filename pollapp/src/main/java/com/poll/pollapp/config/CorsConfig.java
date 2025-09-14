package com.poll.pollapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    /**
     * Create a {@link CorsFilter} to enable cross-origin resource sharing
     * (CORS) for the application.
     *
     * <p>This configuration allows requests from <code>http://localhost:5173</code>
     * to access the application's resources. The allowed HTTP methods are
     * GET, POST, PUT, DELETE, and OPTIONS. All headers are allowed.
     *
     * @return a {@link CorsFilter} to enable CORS for the application
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));


        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
