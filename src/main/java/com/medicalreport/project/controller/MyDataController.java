package com.medicalreport.project.controller;

import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.model.Patient;
import com.medicalreport.project.model.Diagnosis;
import com.medicalreport.project.model.MedicalLeave;
import com.medicalreport.project.repository.MedicalLeaveRepository;
import com.medicalreport.project.repository.MedicalVisitRepository;
import com.medicalreport.project.repository.PatientRepository;
import com.medicalreport.project.repository.DiagnosisRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/my")
public class MyDataController {

    private final PatientRepository patientRepository;
    private final MedicalVisitRepository visitRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final MedicalLeaveRepository leaveRepository;

    public MyDataController(
            PatientRepository patientRepository,
            MedicalVisitRepository visitRepository,
            DiagnosisRepository diagnosisRepository,
            MedicalLeaveRepository leaveRepository
    ) {
        this.patientRepository = patientRepository;
        this.visitRepository = visitRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.leaveRepository = leaveRepository;
    }

    // Returns the patient entity based on EGN; used for "my profile" style views.
    @GetMapping("/info")
    public Patient getMyInfo(@RequestParam String egn) {
        return patientRepository.findByEgn(egn)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // Helper method to resolve the patient once and reuse it across endpoints.
    private Patient findPatient(String egn) {
        return patientRepository.findByEgn(egn)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    // Returns all visits belonging to the patient identified by EGN.
    // Filtering is done in-memory to keep repository interfaces simple.
    @GetMapping("/visits")
    public List<MedicalVisit> getMyVisits(@RequestParam String egn) {
        Patient p = findPatient(egn);

        return visitRepository.findAll().stream()
                .filter(v -> v.getPatient().getId().equals(p.getId()))
                .toList();
    }

    // Returns all diagnoses linked to visits of the given patient.
    @GetMapping("/diagnosis")
    public List<Diagnosis> getMyDiagnosis(@RequestParam String egn) {
        Patient p = findPatient(egn);

        return diagnosisRepository.findAll().stream()
                .filter(d -> d.getVisit().getPatient().getId().equals(p.getId()))
                .toList();
    }

    // Returns all medical leave records issued for visits of the given patient.
    @GetMapping("/leaves")
    public List<MedicalLeave> getMyLeaves(@RequestParam String egn) {
        Patient p = findPatient(egn);

        return leaveRepository.findAll().stream()
                .filter(l -> l.getVisit().getPatient().getId().equals(p.getId()))
                .toList();
    }
}
