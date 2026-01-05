package com.medicalreport.project.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    // Simple endpoint used to verify that ADMIN role authorization works correctly.
    @GetMapping("/test")
    public String adminTest() {
        return "ADMIN ACCESS: Success!";
    }
}
