package com.medicalreport.project.controller;

import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.service.DoctorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping("/add")
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @GetMapping("/all")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
}
