package com.medicalreport.project.repository;

import com.medicalreport.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

// ✅ нужно за updateDoctor (да синхронизираме username/password)
    Optional<User> findByDoctor_Id(Long doctorId);
}
