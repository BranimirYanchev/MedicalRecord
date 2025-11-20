package com.medicalreport.project.service;

import com.medicalreport.project.model.MedicalLeave;
import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.repository.MedicalLeaveRepository;
import com.medicalreport.project.repository.MedicalVisitRepository;
import org.springframework.stereotype.Service;

@Service
public class MedicalLeaveService {

    private final MedicalLeaveRepository leaveRepository;
    private final MedicalVisitRepository visitRepository;

    public MedicalLeaveService(MedicalLeaveRepository leaveRepository, MedicalVisitRepository visitRepository) {
        this.leaveRepository = leaveRepository;
        this.visitRepository = visitRepository;
    }

    public MedicalLeave addLeave(Long visitId, MedicalLeave leave) {

        MedicalVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        leave.setVisit(visit);

        return leaveRepository.save(leave);
    }
}
