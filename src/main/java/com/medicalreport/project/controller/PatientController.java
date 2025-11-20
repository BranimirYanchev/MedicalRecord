package com.medicalreport.project.controller;

import com.medicalreport.project.model.Patient;
import com.medicalreport.project.service.PatientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping("/add")
    public Patient addPatient(@RequestBody Patient patient, @RequestParam Long personalDoctorId) {
        return patientService.addPatient(patient, personalDoctorId);
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @GetMapping("/all")
    public List<Patient> all() {
        return patientService.getAllPatients();
    }
}
