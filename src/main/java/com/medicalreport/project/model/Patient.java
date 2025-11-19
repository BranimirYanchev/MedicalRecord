package com.medicalreport.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String egn;

    private boolean insurancePaid;

    @ManyToOne
    private Doctor personalDoctor;

    @OneToMany(mappedBy = "patient")
    private List<MedicalVisit> visits;
}
