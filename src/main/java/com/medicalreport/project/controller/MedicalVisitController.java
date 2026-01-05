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

    // Creates a new medical visit and links it to both doctor and patient.
    @PostMapping("/add")
    public MedicalVisit addVisit(
            @RequestParam Long doctorId,
            @RequestParam Long patientId,
            @RequestBody MedicalVisit visit
    ) {
        return visitService.createVisit(doctorId, patientId, visit);
    }

    // Retrieves a single visit with its related data.
    @GetMapping("/{id}")
    public MedicalVisit getVisit(@PathVariable Long id) {
        return visitService.getVisit(id);
    }

    // Returns all visits; typically used for lists, dashboards, or admin views.
    @GetMapping("/all")
    public List<MedicalVisit> all() {
        return visitService.getAllVisits();
    }

    // Updates visit details and optionally reassigns doctor and/or patient.
    @PutMapping("/update/{id}")
    public MedicalVisit updateVisit(
            @PathVariable Long id,
            @RequestBody MedicalVisit visit,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long patientId
    ) {
        return visitService.updateVisit(id, visit, doctorId, patientId);
    }

    // Deletes a visit; cascading rules are handled at the service or entity level.
    @DeleteMapping("/delete/{id}")
    public String deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return "Visit deleted successfully";
    }
}
