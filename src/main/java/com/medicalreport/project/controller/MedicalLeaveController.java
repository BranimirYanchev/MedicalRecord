package com.medicalreport.project.controller;

import com.medicalreport.project.model.MedicalLeave;
import com.medicalreport.project.service.MedicalLeaveService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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

    @GetMapping("/{id}")
    public MedicalLeave getLeave(@PathVariable Long id) {
        return leaveService.getLeaveById(id);
    }

    @GetMapping("/all")
    public List<MedicalLeave> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    @PutMapping("/update/{id}")
    public MedicalLeave updateLeave(
            @PathVariable Long id,
            @RequestBody MedicalLeave leave,
            @RequestParam(required = false) Long visitId
    ) {
        return leaveService.updateLeave(id, leave, visitId);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        return "Medical leave deleted successfully";
    }
}
