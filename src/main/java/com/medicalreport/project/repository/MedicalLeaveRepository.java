package com.medicalreport.project.repository;

import com.medicalreport.project.model.MedicalLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalLeaveRepository extends JpaRepository<MedicalLeave, Long> {
}
