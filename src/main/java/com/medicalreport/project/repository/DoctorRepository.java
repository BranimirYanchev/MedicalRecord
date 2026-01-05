package com.medicalreport.project.repository;

import com.medicalreport.project.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByActiveTrue();
}
