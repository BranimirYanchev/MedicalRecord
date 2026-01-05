package com.medicalreport.project.service;

import com.medicalreport.project.model.MedicalLeave;
import com.medicalreport.project.model.MedicalVisit;
import com.medicalreport.project.repository.MedicalLeaveRepository;
import com.medicalreport.project.repository.MedicalVisitRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicalLeaveService {

    private final MedicalLeaveRepository leaveRepository;
    private final MedicalVisitRepository visitRepository;

    public MedicalLeaveService(MedicalLeaveRepository leaveRepository,
                               MedicalVisitRepository visitRepository) {
        this.leaveRepository = leaveRepository;
        this.visitRepository = visitRepository;
    }

    public MedicalLeave addLeave(Long visitId, MedicalLeave leave) {

        MedicalVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        leave.setVisit(visit);
        return leaveRepository.save(leave);
    }

    public MedicalLeave getLeaveById(Long id) {
        return leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical leave not found"));
    }

    public List<MedicalLeave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public MedicalLeave updateLeave(Long id, MedicalLeave updated, Long visitId) {
        MedicalLeave existing = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical leave not found"));

        existing.setDays(updated.getDays());
        existing.setStartDate(updated.getStartDate());

        if (visitId != null) {
            MedicalVisit visit = visitRepository.findById(visitId)
                    .orElseThrow(() -> new RuntimeException("Visit not found"));
            existing.setVisit(visit);
        }

        return leaveRepository.save(existing);
    }


    public void deleteLeave(Long id) {
        MedicalLeave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical leave not found"));

        // 🔥 ВАЖНО: скъсваме връзката от parent-а
        MedicalVisit visit = leave.getVisit();
        if (visit != null) {
            visit.setMedicalLeave(null);
            visitRepository.save(visit);
        }

        // 🔥 Сега Hibernate може да изтрие child-а
        leaveRepository.delete(leave);
    }

}
