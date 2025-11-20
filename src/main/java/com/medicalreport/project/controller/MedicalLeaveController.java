package com.medicalreport.project.controller;

import com.medicalreport.project.model.MedicalLeave;
import com.medicalreport.project.service.MedicalLeaveService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leave")
public class MedicalLeaveController {

    private final MedicalLeaveService leaveService;

    public MedicalLeaveController(MedicalLeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @PostMapping("/add")
    public MedicalLeave addLeave(
            @RequestParam Long visitId,
            @RequestBody MedicalLeave leave
    ) {
        return leaveService.addLeave(visitId, leave);
    }
}
