package com.medicalreport.project.service;

import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.model.Patient;
import com.medicalreport.project.repository.MedicalVisitRepository;
import com.medicalreport.project.repository.DoctorRepository;
import com.medicalreport.project.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalVisitService {

    private final MedicalVisitRepository visitRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public MedicalVisitService(
            MedicalVisitRepository visitRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository
    ) {
        this.visitRepository = visitRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public MedicalVisit createVisit(Long doctorId, Long patientId, MedicalVisit visit) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        visit.setDoctor(doctor);
        visit.setPatient(patient);

        return visitRepository.save(visit);
    }

    public MedicalVisit getVisit(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
    }

    public List<MedicalVisit> getAllVisits() {
        return visitRepository.findAll();
    }
}
