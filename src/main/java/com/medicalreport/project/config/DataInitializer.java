package com.medicalreport.project.config;

import com.medicalreport.project.model.Role;
import com.medicalreport.project.model.User;
import com.medicalreport.project.repository.RoleRepository;
import com.medicalreport.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            final String adminUsername = "admin";
            final String adminPass = "admin123";

            // Ensure the ADMIN role exists before seeding users that depend on it.
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

            // Create a default admin user only if it doesn't already exist (safe to run on every startup).
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode(adminPass));
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
                System.out.println("🟢 Admin user created: admin / admin123");
            }
        };
    }
}
