package com.medicalreport.project.service;

import com.medicalreport.project.model.Diagnosis;
import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.repository.DiagnosisRepository;
import com.medicalreport.project.repository.MedicalVisitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final MedicalVisitRepository visitRepository;

    public DiagnosisService(DiagnosisRepository diagnosisRepository,
                            MedicalVisitRepository visitRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.visitRepository = visitRepository;
    }

    public Diagnosis addDiagnosis(Long visitId, Diagnosis diagnosis) {
        MedicalVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        diagnosis.setVisit(visit);
        return diagnosisRepository.save(diagnosis);
    }

    public Diagnosis getDiagnosisById(Long id) {
        return diagnosisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Diagnosis not found"));
    }

    public List<Diagnosis> getAllDiagnoses() {
        return diagnosisRepository.findAll();
    }

    public Diagnosis updateDiagnosis(Long id, Diagnosis updated, Long visitId) {
        Diagnosis existing = getDiagnosisById(id);

        existing.setName(updated.getName());

        if (visitId != null) {
            MedicalVisit visit = visitRepository.findById(visitId)
                    .orElseThrow(() -> new RuntimeException("Visit not found"));
            existing.setVisit(visit);
        }

        return diagnosisRepository.save(existing);
    }

    // ✔ DELETE
    public void deleteDiagnosis(Long id) {
        if (!diagnosisRepository.existsById(id)) {
            throw new RuntimeException("Diagnosis not found");
        }
        diagnosisRepository.deleteById(id);
    }
}
