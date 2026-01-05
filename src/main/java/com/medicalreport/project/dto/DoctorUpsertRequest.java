package com.medicalreport.project.dto;

import lombok.Data;

@Data
public class DoctorUpsertRequest {
    private String name;
    private String specialty;
    private boolean personalDoctor;

    // OPTIONAL при update (ако е празно/нула → не сменяме)
    // REQUIRED при add (ще проверим в service)
    private String password;
}
