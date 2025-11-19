package com.medicalreport.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class MedicalLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int days;

    private LocalDate startDate;

    @OneToOne
    @JoinColumn(name = "visit_id")
    private MedicalVisit visit;
}
