package com.medicalreport.project.controller;

import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.model.Patient;
import com.medicalreport.project.service.ReportsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/patients-by-diagnosis")
    public List<Patient> patientsByDiagnosis(@RequestParam String name) {
        return reportsService.getPatientsByDiagnosis(name);
    }

    @GetMapping("/most-common-diagnosis")
    public Map<String, String> mostCommonDiagnosis() {
        return Map.of("value", reportsService.getMostCommonDiagnosis());
    }

    @GetMapping("/patients-by-doctor")
    public List<Patient> patientsByDoctor(@RequestParam Long doctorId) {
        return reportsService.getPatientsByPersonalDoctor(doctorId);
    }

    @GetMapping("/count-patients-per-doctor")
    public Map<String, Long> countPatientsPerDoctor() {
        return reportsService.countPatientsPerDoctor();
    }

    @GetMapping("/count-visits-per-doctor")
    public Map<String, Long> countVisitsPerDoctor() {
        return reportsService.countVisitsPerDoctor();
    }

    @GetMapping("/visits-of-patient")
    public List<MedicalVisit> visitsOfPatient(@RequestParam Long patientId) {
        return reportsService.getVisitsOfPatient(patientId);
    }

    @GetMapping("/visits-period")
    public List<MedicalVisit> visitsInPeriod(
            @RequestParam String start,
            @RequestParam String end
    ) {
        return reportsService.getVisitsInPeriod(LocalDate.parse(start), LocalDate.parse(end));
    }

    @GetMapping("/visits-doctor-period")
    public List<MedicalVisit> visitsDoctorPeriod(
            @RequestParam Long doctorId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        return reportsService.getVisitsForDoctorInPeriod(
                doctorId,
                LocalDate.parse(start),
                LocalDate.parse(end)
        );
    }

    @GetMapping("/month-most-leaves")
    public Month monthMostLeaves() {
        return reportsService.getMonthWithMostLeaves();
    }

    @GetMapping("/doctors-most-leaves")
    public Map<String, Long> doctorsMostLeaves() {
        return reportsService.getDoctorsWithMostLeaves();
    }

    // 11) NEW — summary for overview table
    @GetMapping("/overview")
    public List<Map<String, Object>> overview() {
        return reportsService.getOverviewData();
    }
}
