package com.medicalreport.project.service;

import com.medicalreport.project.model.Patient;
import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.repository.PatientRepository;
import com.medicalreport.project.repository.DoctorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return patientRepository.findByActiveTrue();
    }

    // ✔ Оправен update метод
    public Patient updatePatient(Long id, Patient updated, Long personalDoctorId) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Update basic fields
        existing.setName(updated.getName());
        existing.setEgn(updated.getEgn());
        existing.setInsurancePaid(updated.isInsurancePaid());

        // Update personal doctor (if provided)
        if (personalDoctorId != null) {
            Doctor doctor = doctorRepository.findById(personalDoctorId)
                    .orElseThrow(() -> new RuntimeException("Doctor not found"));
            existing.setPersonalDoctor(doctor);
        }

        return patientRepository.save(existing);
    }

    @Transactional
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setActive(false);
        patientRepository.save(patient);
    }

}
