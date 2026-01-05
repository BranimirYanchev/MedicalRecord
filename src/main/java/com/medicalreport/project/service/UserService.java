package com.medicalreport.project.service;

import com.medicalreport.project.model.Doctor;
import com.medicalreport.project.model.Patient;
import com.medicalreport.project.model.Role;
import com.medicalreport.project.model.User;
import com.medicalreport.project.repository.DoctorRepository;
import com.medicalreport.project.repository.PatientRepository;
import com.medicalreport.project.repository.RoleRepository;
import com.medicalreport.project.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✔ REGISTRATION — ADMIN
    public User registerAdmin(String username, String password) {

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        user.setRoles(Set.of(adminRole));

        return userRepository.save(user);
    }

    // ✔ REGISTRATION — DOCTOR
    public User registerDoctorUser(String username, String password, Long doctorId) {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role doctorRole = roleRepository.findByName("ROLE_DOCTOR")
                .orElseThrow(() -> new RuntimeException("ROLE_DOCTOR not found"));

        user.setRoles(Set.of(doctorRole));
        user.setDoctor(doctor);

        return userRepository.save(user);
    }

    // ✔ REGISTRATION — PATIENT
    public User registerPatientUser(String username, String password, Long patientId) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role patientRole = roleRepository.findByName("ROLE_PATIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_PATIENT not found"));

        user.setRoles(Set.of(patientRole));
        user.setPatient(patient);

        return userRepository.save(user);
    }
}
