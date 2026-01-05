package com.medicalreport.project.repository;

import com.medicalreport.project.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;


public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEgn(String egn);

    long countByPersonalDoctorId(Long id);

    List<Patient> findByActiveTrue();
}
