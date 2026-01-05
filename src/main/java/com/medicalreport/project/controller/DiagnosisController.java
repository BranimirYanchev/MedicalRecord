package com.medicalreport.project.controller;

import com.medicalreport.project.model.Diagnosis;
import com.medicalreport.project.service.DiagnosisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    // Creates a diagnosis and attaches it to an existing visit via visitId.
    @PostMapping("/add")
    public Diagnosis addDiagnosis(@RequestParam Long visitId, @RequestBody Diagnosis diagnosis) {
        return diagnosisService.addDiagnosis(visitId, diagnosis);
    }

    // Retrieves a single diagnosis by its identifier.
    @GetMapping("/{id}")
    public Diagnosis getDiagnosis(@PathVariable Long id) {
        return diagnosisService.getDiagnosisById(id);
    }

    // Returns all diagnoses; mainly intended for administrative or overview purposes.
    @GetMapping("/all")
    public List<Diagnosis> getAllDiagnoses() {
        return diagnosisService.getAllDiagnoses();
    }

    // Updates diagnosis data and optionally reassigns it to a different visit.
    @PutMapping("/update/{id}")
    public Diagnosis updateDiagnosis(
            @PathVariable Long id,
            @RequestBody Diagnosis diagnosis,
            @RequestParam(required = false) Long visitId
    ) {
        return diagnosisService.updateDiagnosis(id, diagnosis, visitId);
    }

    // Removes a diagnosis permanently from the system.
    @DeleteMapping("/delete/{id}")
    public String deleteDiagnosis(@PathVariable Long id) {
        diagnosisService.deleteDiagnosis(id);
        return "Diagnosis deleted successfully";
    }
}
