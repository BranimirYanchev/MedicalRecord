package com.medicalreport.project.repository;

import com.medicalreport.project.model.MedicalVisit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalVisitRepository extends JpaRepository<MedicalVisit, Long> {
}
