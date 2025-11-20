package com.medicalreport.project.controller;

import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.service.MedicalVisitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visit")
public class MedicalVisitController {

    private final MedicalVisitService visitService;

    public MedicalVisitController(MedicalVisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/add")
    public MedicalVisit addVisit(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestBody MedicalVisit visit
    ) {
        return visitService.createVisit(doctorId, patientId, visit);
    }

    @GetMapping("/{id}")
    public MedicalVisit getVisit(@PathVariable Long id) {
        return visitService.getVisit(id);
    }

    @GetMapping("/all")
    public List<MedicalVisit> all() {
        return visitService.getAllVisits();
    }
}
