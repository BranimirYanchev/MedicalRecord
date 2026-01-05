package com.medicalreport.project.service;

import com.medicalreport.project.dto.DoctorUpsertRequest;
import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.model.Role;
import com.medicalreport.project.model.User;
import com.medicalreport.project.repository.DoctorRepository;
import com.medicalreport.project.repository.RoleRepository;
import com.medicalreport.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medicalreport.project.repository.PatientRepository;

import java.util.List;
import java.util.Set;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DoctorService(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findByActiveTrue();
    }

    // =========================
    // ADD Doctor + create User
    // =========================
    public Doctor addDoctorWithUser(DoctorUpsertRequest req) {
        String name = safe(req.getName());
        String specialty = safe(req.getSpecialty());
        String rawPassword = req.getPassword() == null ? "" : req.getPassword().trim();

        if (name.isEmpty()) throw new RuntimeException("Name is required");
        if (specialty.isEmpty()) throw new RuntimeException("Specialty is required");
        if (rawPassword.isEmpty()) throw new RuntimeException("Password is required for doctor login");

        // username = name (както искаш: име + парола)
        if (userRepository.findByUsername(name).isPresent()) {
            throw new RuntimeException("User with this doctor name already exists (username must be unique): " + name);
        }

        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setSpecialty(specialty);
        doctor.setPersonalDoctor(req.isPersonalDoctor());
        doctor = doctorRepository.save(doctor);

        Role doctorRole = roleRepository.findByName("ROLE_DOCTOR")
                .orElseThrow(() -> new RuntimeException("ROLE_DOCTOR not found"));

        User user = new User();
        user.setUsername(name);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(doctorRole));
        user.setDoctor(doctor);

        userRepository.save(user);

        return doctor;
    }

    // ==========================================
    // UPDATE Doctor + sync User (username/pass)
    // ==========================================
    public Doctor updateDoctorWithUser(Long id, DoctorUpsertRequest req) {
        Doctor existing = getDoctorById(id);

        String newName = safe(req.getName());
        String newSpec = safe(req.getSpecialty());
        boolean newPD = req.isPersonalDoctor();

        if (newName.isEmpty()) throw new RuntimeException("Name is required");
        if (newSpec.isEmpty()) throw new RuntimeException("Specialty is required");

        String oldName = existing.getName();

        existing.setName(newName);
        existing.setSpecialty(newSpec);
        existing.setPersonalDoctor(newPD);

        Doctor saved = doctorRepository.save(existing);

        // Ако имаме user към този doctor → синхронизираме username и/или password
        userRepository.findByDoctor_Id(saved.getId()).ifPresent(user -> {

            // ако сменяме името → сменяме username (и пазим уникалност)
            if (!oldName.equals(newName)) {
                userRepository.findByUsername(newName).ifPresent(u -> {
                    // ако е същият user → ok, ако е друг → error
                    if (!u.getId().equals(user.getId())) {
                        throw new RuntimeException("Username already taken: " + newName);
                    }
                });
                user.setUsername(newName);
            }

            // ако има подадена password (непразна) → сменяме
            if (req.getPassword() != null && !req.getPassword().trim().isEmpty()) {
                user.setPassword(passwordEncoder.encode(req.getPassword().trim()));
            }

            userRepository.save(user);
        });

        return saved;
    }

    @Transactional
    public void deleteDoctor(Long id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        doctor.setActive(false);
        doctorRepository.save(doctor);
    }


    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
