package com.eps.sitemanager.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig {
    /**
     * CORS configuration for Spring Web MVC
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                       .allowedMethods("GET", "POST")
                       .allowedOrigins("http://localhost:3000", "http://localhost:8090");
            }
        };
    }
}
