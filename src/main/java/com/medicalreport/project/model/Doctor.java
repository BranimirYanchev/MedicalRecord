package com.medicalreport.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String specialty;

    private boolean personalDoctor; // дали е личен лекар

    @OneToMany(mappedBy = "doctor")
    private List<MedicalVisit> visits;
}
