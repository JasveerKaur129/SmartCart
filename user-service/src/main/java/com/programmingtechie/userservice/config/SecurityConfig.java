package com.programmingtechie.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF for stateless authentication (JWT)
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for JWT authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/user/signup", "/api/user/login").permitAll()  // Allow public access for signup and login
                .anyRequest().authenticated()  // Other requests require authentication
            );

        return http.build();
    }
}
