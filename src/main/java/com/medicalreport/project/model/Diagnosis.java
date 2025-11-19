package com.medicalreport.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "visit_id")
    private MedicalVisit visit;
}
