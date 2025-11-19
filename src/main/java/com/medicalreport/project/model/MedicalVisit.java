package com.medicalreport.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class MedicalVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate visitDate;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private Diagnosis diagnosis;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private MedicalLeave medicalLeave;
}
