package com.medicalreport.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // изключва CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()   // позволява всички заявки
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
