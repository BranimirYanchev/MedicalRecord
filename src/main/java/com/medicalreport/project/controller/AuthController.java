package com.medicalreport.project.controller;

import com.medicalreport.project.model.User;
import com.medicalreport.project.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            HttpServletRequest request,
            @RequestParam String username,
            @RequestParam String password
    ) {
        // Delegate credential validation to Spring Security authentication pipeline.
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Store authentication in the SecurityContext so it is available for the current request.
        SecurityContextHolder.getContext().setAuthentication(auth);

        // Explicitly persist the SecurityContext in the HTTP session to enable session-based auth.
        request.getSession(true)
                .setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        // Extract the primary role; assumes one main role per user.
        String role = auth.getAuthorities().iterator().next().getAuthority();

        // Load the User entity instance (not just the security principal)
        // to access domain-specific relations like doctor/patient.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Build a minimal response used by the frontend to route the user correctly.
        Map<String, Object> res = new HashMap<>();
        res.put("role", role);

        if (user.getDoctor() != null) res.put("doctorId", user.getDoctor().getId());
        if (user.getPatient() != null) res.put("patientId", user.getPatient().getId());

        return res;
    }
}
