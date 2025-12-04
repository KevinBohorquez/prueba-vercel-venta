package com.venta.backend.config;

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

        config.addAllowedOriginPattern("*"); // permite todos los orígenes (!)
        config.addAllowedMethod("*");        // permite todos los métodos
        config.addAllowedHeader("*");        // permite todos los headers
        config.setAllowCredentials(false);   // DEBE ser false si usas "*"

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
