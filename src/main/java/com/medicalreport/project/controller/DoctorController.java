package com.medicalreport.project.controller;

import com.medicalreport.project.dto.DoctorUpsertRequest;
import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.service.DoctorService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // Returns all doctors; used for listings, selection dialogs, and admin views.
    @GetMapping("/all")
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // Fetches a single doctor by ID.
    @GetMapping("/{id}")
    public Doctor getDoctor(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    // Creates a doctor together with the associated User account in one operation.
    @PostMapping("/add")
    public Doctor addDoctor(@RequestBody DoctorUpsertRequest req) {
        return doctorService.addDoctorWithUser(req);
    }

    // Updates both doctor data and related user credentials/roles if provided.
    @PutMapping("/update/{id}")
    public Doctor updateDoctor(@PathVariable Long id,
                               @RequestBody DoctorUpsertRequest req) {
        return doctorService.updateDoctorWithUser(id, req);
    }

    // Soft-deletes or deactivates a doctor; business rules may block deletion if constraints exist.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok("Записът е деактивиран");
        } catch (IllegalStateException e) {
            // Thrown when domain rules prevent deletion (e.g. active relations).
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            // Thrown when the doctor does not exist.
            return ResponseEntity.notFound().build();
        }
    }
}
