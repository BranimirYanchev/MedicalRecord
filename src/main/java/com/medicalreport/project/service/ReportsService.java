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

    // a) Списък с пациенти, с дадена диагноза.
    public List<Patient> getPatientsByDiagnosis(String diagnosisName) {
        return diagnosisRepository.findAll().stream()
                .filter(d -> d.getName() != null &&
                        d.getName().equalsIgnoreCase(diagnosisName))
                .map(d -> d.getVisit().getPatient())
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    // b) Най-често диагностицирана диагноза.
    public String getMostCommonDiagnosis() {
        return diagnosisRepository.findAll().stream()
                .filter(d -> d.getName() != null)
                .collect(Collectors.groupingBy(
                        Diagnosis::getName,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Няма данни");
    }

    // c) Списък с пациенти, които имат даден личен лекар.
    public List<Patient> getPatientsByPersonalDoctor(Long doctorId) {
        return patientRepository.findAll().stream()
                .filter(p -> p.getPersonalDoctor() != null)
                .filter(p -> p.getPersonalDoctor().getId().equals(doctorId))
                .collect(Collectors.toList());
    }

    // d) Брой пациенти при всеки личен лекар.
    public Map<String, Long> countPatientsPerDoctor() {
        return patientRepository.findAll().stream()
                .filter(p -> p.getPersonalDoctor() != null)
                .collect(Collectors.groupingBy(
                        p -> p.getPersonalDoctor().getName(),
                        Collectors.counting()
                ));
    }

    // e) Брой посещения при всеки от лекарите.
    public Map<String, Long> countVisitsPerDoctor() {
        return visitRepository.findAll().stream()
                .filter(v -> v.getDoctor() != null)
                .collect(Collectors.groupingBy(
                        v -> v.getDoctor().getName(),
                        Collectors.counting()
                ));
    }

    // f) Списък с посещения на всеки пациент.
    public List<MedicalVisit> getVisitsOfPatient(Long patientId) {
        return visitRepository.findAll().stream()
                .filter(v -> v.getPatient() != null &&
                        v.getPatient().getId().equals(patientId))
                .collect(Collectors.toList());
    }

    // g) Списък на прегледите при всички лекари в даден период.
    public List<MedicalVisit> getVisitsInPeriod(LocalDate start, LocalDate end) {
        return visitRepository.findAll().stream()
                .filter(v -> v.getVisitDate() != null)
                .filter(v -> !v.getVisitDate().isBefore(start) &&
                        !v.getVisitDate().isAfter(end))
                .collect(Collectors.toList());
    }

    // h) Списък на прегледите при определен лекар за даден период.
    public List<MedicalVisit> getVisitsForDoctorInPeriod(Long doctorId,
                                                         LocalDate start,
                                                         LocalDate end) {
        return visitRepository.findAll().stream()
                .filter(v -> v.getDoctor() != null &&
                        v.getDoctor().getId().equals(doctorId))
                .filter(v -> v.getVisitDate() != null &&
                        !v.getVisitDate().isBefore(start) &&
                        !v.getVisitDate().isAfter(end))
                .collect(Collectors.toList());
    }

    // i) Месецът с най-много болнични.
    public Month getMonthWithMostLeaves() {
        return leaveRepository.findAll().stream()
                .filter(l -> l.getStartDate() != null)
                .collect(Collectors.groupingBy(
                        l -> l.getStartDate().getMonth(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    // j) Лекарите, които са издали най-много болнични.
    public Map<String, Long> getDoctorsWithMostLeaves() {
        return leaveRepository.findAll().stream()
                .filter(l -> l.getVisit() != null &&
                        l.getVisit().getDoctor() != null)
                .collect(Collectors.groupingBy(
                        l -> l.getVisit().getDoctor().getName(),
                        Collectors.counting()
                ));
    }

    public List<Map<String, Object>> getOverviewData() {

        List<Map<String, Object>> list = new ArrayList<>();

        // 1) Брой пациенти към всеки личен лекар
        Map<String, Long> patientsPerDoctor = countPatientsPerDoctor();
        patientsPerDoctor.forEach((doctor, count) -> {
            list.add(Map.of(
                    "type", "Пациенти",
                    "description", "Личен лекар: " + doctor,
                    "count", count
            ));
        });

        // 2) Брой посещения при всеки лекар
        Map<String, Long> visitsPerDoctor = countVisitsPerDoctor();
        visitsPerDoctor.forEach((doctor, count) -> {
            list.add(Map.of(
                    "type", "Посещения",
                    "description", "Лекар: " + doctor,
                    "count", count
            ));
        });

        // 3) Най-честа диагноза
        String diag = getMostCommonDiagnosis();
        list.add(Map.of(
                "type", "Диагнози",
                "description", "Най-честа диагноза",
                "count", diag
        ));

        // 4) Лекари с най-много болнични
        Map<String, Long> leaves = getDoctorsWithMostLeaves();
        leaves.forEach((doctor, count) -> {
            list.add(Map.of(
                    "type", "Болнични",
                    "description", "Издадени болнични от: " + doctor,
                    "count", count
            ));
        });

        // 5) Месец с най-много болнични
        Month month = getMonthWithMostLeaves();
        list.add(Map.of(
                "type", "Болнични",
                "description", "Месец с най-много болнични",
                "count", month != null ? month.toString() : "—"
        ));

        return list;
    }
}
