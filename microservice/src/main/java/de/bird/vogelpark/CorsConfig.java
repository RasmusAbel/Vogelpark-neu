package de.bird.vogelpark;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // Erlaubt Zugriff von allen Ursprüngen
        config.addAllowedMethod("*"); // Erlaubt alle HTTP-Methoden (GET, POST, PUT, DELETE usw.)
        config.addAllowedHeader("*"); // Erlaubt alle Header
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}

