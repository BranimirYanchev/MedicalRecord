package com.medicalreport.project.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalLeave medicalLeave;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private Diagnosis diagnosis;
}
