package com.medicalreport.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // CSRF is disabled because the app uses a custom login flow/pages (not Spring Security form login).
                // If you later add state-changing endpoints accessed from browser forms, consider enabling CSRF.
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Allow static assets to load without authentication (otherwise login page breaks visually).
                        .requestMatchers(
                                "/pages/**",
                                "/styles/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()

                        .requestMatchers("/auth/login").permitAll()

                        // Public read-only endpoints (patient-facing views, listings, etc.).
                        .requestMatchers(
                                "/my/**",
                                "/visit/all",
                                "/doctor/all",
                                "/patient/all"
                        ).permitAll()

                        // Keep the most specific patterns/rules above broader ones to avoid accidental access changes.
                        .requestMatchers(
                                "/visit/*",          // GET /visit/{id}
                                "/visit/add",
                                "/visit/update/*"
                        ).hasAnyRole("DOCTOR", "ADMIN")

                        .requestMatchers(
                                "/visit/delete/*"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/diagnosis/all",
                                "/diagnosis/add",
                                "/diagnosis/update/*"
                        ).hasAnyRole("DOCTOR", "ADMIN")

                        .requestMatchers(
                                "/leave/all",
                                "/leave/add",
                                "/leave/update/*"
                        ).hasAnyRole("DOCTOR", "ADMIN")

                        .requestMatchers(
                                "/doctor/**",
                                "/patient/**",
                                "/admin/**",
                                "/reports/**"
                        ).hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                // Disable built-in auth mechanisms because login is handled by custom endpoints/pages.
                .httpBasic(basic -> basic.disable())
                .formLogin(form -> form.disable())

                // Use server-side sessions; redirect to login page when session is missing/expired.
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                .invalidSessionUrl("/pages/login.html")
                )

                // For unauthenticated requests, send users to the custom login page.
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint((req, res, e) ->
                                res.sendRedirect("/pages/login.html"))
                );

        return http.build();
    }

    // BCrypt is a safe default for hashing passwords (adaptive cost).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose AuthenticationManager so custom login endpoint/service can authenticate credentials.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
