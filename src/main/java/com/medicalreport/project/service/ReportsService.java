package com.medicalreport.project.service;

import com.medicalreport.project.model.*;
import com.medicalreport.project.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportsService {

    private final DiagnosisRepository diagnosisRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final MedicalVisitRepository visitRepository;
    private final MedicalLeaveRepository leaveRepository;

    public ReportsService(DiagnosisRepository diagnosisRepository,
                          PatientRepository patientRepository,
                          DoctorRepository doctorRepository,
                          MedicalVisitRepository visitRepository,
                          MedicalLeaveRepository leaveRepository) {

        this.diagnosisRepository = diagnosisRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.visitRepository = visitRepository;
        this.leaveRepository = leaveRepository;
    }

    // 1) Patients with a given diagnosis
    public List<Patient> getPatientsByDiagnosis(String diagnosisName) {
        return diagnosisRepository.findAll().stream()
                .filter(d -> d.getName().equalsIgnoreCase(diagnosisName))
                .map(d -> d.getVisit().getPatient())
                .distinct()
                .collect(Collectors.toList());
    }

    // 2) Most common diagnosis
    public String getMostCommonDiagnosis() {
        return diagnosisRepository.findAll().stream()
                .collect(Collectors.groupingBy(Diagnosis::getName, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    // 3) Patients with given personal doctor
    public List<Patient> getPatientsByPersonalDoctor(Long doctorId) {
        return patientRepository.findAll().stream()
                .filter(p -> p.getPersonalDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }

    // 4) Count patients per personal doctor
    public Map<String, Long> countPatientsPerDoctor() {
        return patientRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPersonalDoctor().getName(),
                        Collectors.counting()
                ));
    }

    // 5) Count visits per doctor
    public Map<String, Long> countVisitsPerDoctor() {
        return visitRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        v -> v.getDoctor().getName(),
                        Collectors.counting()
                ));
    }

    // 6) All visits of a patient
    public List<MedicalVisit> getVisitsOfPatient(Long patientId) {
        return visitRepository.findAll().stream()
                .filter(v -> v.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    // 7) Visits for all doctors in a period
    public List<MedicalVisit> getVisitsInPeriod(LocalDate start, LocalDate end) {
        return visitRepository.findAll().stream()
                .filter(v -> !v.getVisitDate().isBefore(start) &&
                        !v.getVisitDate().isAfter(end))
                .collect(Collectors.toList());
    }

    // 8) Visits for one doctor in a period
    public List<MedicalVisit> getVisitsForDoctorInPeriod(Long doctorId, LocalDate start, LocalDate end) {
        return visitRepository.findAll().stream()
                .filter(v -> v.getDoctor().getId().equals(doctorId))
                .filter(v -> !v.getVisitDate().isBefore(start) &&
                        !v.getVisitDate().isAfter(end))
                .collect(Collectors.toList());
    }

    // 9) Month with the most medical leaves
    public Month getMonthWithMostLeaves() {
        return leaveRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        l -> l.getStartDate().getMonth(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // 10) Doctors who issued the most medical leaves
    public Map<String, Long> getDoctorsWithMostLeaves() {
        return leaveRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        l -> l.getVisit().getDoctor().getName(),
                        Collectors.counting()
                ));
    }
}
