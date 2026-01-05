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

    // Creates a new patient and assigns a personal doctor via doctor ID.
    @PostMapping("/add")
    public Patient addPatient(
            @RequestBody Patient patient,
            @RequestParam Long personalDoctorId
    ) {
        return patientService.addPatient(patient, personalDoctorId);
    }

    // Retrieves a patient by ID.
    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    // Returns only active patients; soft-deleted records are excluded.
    @GetMapping("/all")
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    // Updates patient details and optionally changes the assigned personal doctor.
    @PutMapping("/update/{id}")
    public Patient updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient,
            @RequestParam(required = false) Long personalDoctorId
    ) {
        return patientService.updatePatient(id, patient, personalDoctorId);
    }

    // Soft-deletes a patient record so historical data remains intact.
    @DeleteMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return "Записът е деактивиран";
    }
}
