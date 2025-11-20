package com.medicalreport.project.controller;

import com.medicalreport.project.model.Diagnosis;
import com.medicalreport.project.service.DiagnosisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    private final DiagnosisService diagnosisService;

    public DiagnosisController(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PostMapping("/add")
    public Diagnosis addDiagnosis(
            @RequestParam Long visitId,
            @RequestBody Diagnosis diagnosis
    ) {
        return diagnosisService.addDiagnosis(visitId, diagnosis);
    }
}
