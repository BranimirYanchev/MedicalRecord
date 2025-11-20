package com.medicalreport.project.service;

import com.medicalreport.project.model.Patient;
import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.repository.PatientRepository;
import com.medicalreport.project.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientService(PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    public Patient addPatient(Patient patient, Long personalDoctorId) {
        Doctor doctor = doctorRepository.findById(personalDoctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        patient.setPersonalDoctor(doctor);

        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }
}
