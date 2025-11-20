package com.medicalreport.project.service;

import com.medicalreport.project.model.Diagnosis;
import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.repository.DiagnosisRepository;
import com.medicalreport.project.repository.MedicalVisitRepository;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final MedicalVisitRepository visitRepository;

    public DiagnosisService(DiagnosisRepository diagnosisRepository, MedicalVisitRepository visitRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.visitRepository = visitRepository;
    }

    public Diagnosis addDiagnosis(Long visitId, Diagnosis diagnosis) {

        MedicalVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        diagnosis.setVisit(visit);

        return diagnosisRepository.save(diagnosis);
    }
}
