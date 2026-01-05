package com.medicalreport.project.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
    @JsonIgnore
    private List<MedicalVisit> visits;

    private boolean active = true;

}
