package com.c102.ourotest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security Configuration for testing Ouroboros Mock Filters compatibility
 *
 * This simulates a REAL production environment where the user has configured security.
 * All API endpoints require authentication to test the filter chain order.
 *
 * Filter Chain Order (Order: smaller number = higher priority):
 * 1. Ouroboros Mock Filters (Order = -101) ← Executes FIRST!
 *    - MockRoutingFilter: Check if endpoint is in mock registry
 *    - MockValidationFilter: Validate request (if mocked)
 *    - MockResponseFilter: Return mock data (if mocked)
 * 2. Spring Security Filters (Order = -100) ← Executes SECOND
 *    - Authentication check
 * 3. Real Controller (if not mocked and authenticated)
 *
 * Test Scenarios:
 * - Mock endpoint WITHOUT auth → 200 OK (Mock filter handles BEFORE Security) ✅
 * - Mock endpoint WITH auth → 200 OK (Mock filter handles BEFORE Security) ✅
 * - Non-mock endpoint WITHOUT auth → 401 Unauthorized (Security blocks) ✅
 * - Non-mock endpoint WITH auth → 200 OK (reaches real controller) ✅
 *
 * Expected Behavior:
 * Mock endpoints (x-ouroboros-progress: mock) BYPASS authentication because
 * MockFilter runs BEFORE Security filter!
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
            .authorizeHttpRequests(auth -> auth
                // OpenAPI/SpringDoc endpoints - must be public for Ouroboros library
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // ALL API endpoints require authentication
                // This simulates a real production environment
                .requestMatchers("/api/**").authenticated()

                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {}); // Enable HTTP Basic Authentication

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .build();

        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("USER", "ADMIN")
            .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}