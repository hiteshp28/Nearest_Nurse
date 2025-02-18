package com.seniorcare.seniorCitizenAlert.controller;

import com.seniorcare.seniorCitizenAlert.entity.NurseEntity;
import com.seniorcare.seniorCitizenAlert.repository.NurseRepo;
import com.seniorcare.seniorCitizenAlert.service.NurseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Component
@RestController
@RequestMapping("/nurse")
public class NurseController {
    @Autowired
    private NurseService nurseService;

    @Autowired
    private NurseRepo nurseRepo;

    @GetMapping("/get-all")
    public List<NurseEntity> getAllNurse()
    {
        List<NurseEntity> all = nurseService.getAll();
        return all;

    }
    @PostMapping("/register")
    public NurseEntity createNurse(@RequestBody NurseEntity nurse)
    {
        return nurseService.saveNewNurse(nurse);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody NurseEntity nurse) {
        Optional<NurseEntity> existingNurse = Optional.ofNullable(nurseRepo.findByUserName(nurse.getUserName()));
        if (existingNurse.isPresent()) {
            NurseEntity updatedNurse = existingNurse.get();
            updatedNurse.setStatus("Available");
            nurseRepo.save(updatedNurse);
            return ResponseEntity.ok("Login successful. Status set to Available.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nurse not found");
    }


    @PostMapping("/logout/{nurseId}")
    public ResponseEntity<String> logout(@PathVariable ObjectId nurseId) {
        Optional<NurseEntity> nurse = nurseRepo.findById(nurseId);
        if (nurse.isPresent()) {
            NurseEntity updatedNurse = nurse.get();
            updatedNurse.setStatus("Offline");
            nurseRepo.save(updatedNurse);
            return ResponseEntity.ok("Logout successful. Status set to Offline.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nurse not found");
    }

    @PostMapping("/acceptRequest/{nurseId}")
    public ResponseEntity<String> acceptRequest(@PathVariable ObjectId nurseId) {
        Optional<NurseEntity> nurse = nurseRepo.findById(nurseId);

        if (nurse.isPresent()) {
            NurseEntity updatedNurse = nurse.get();
            if (!"Pending".equals(updatedNurse.getStatus())) {
                return ResponseEntity.badRequest().body("Request expired or already handled.");
            }
            updatedNurse.setStatus("Booked");
            nurseRepo.save(updatedNurse);
            return ResponseEntity.ok("Request accepted. Nurse assigned.");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nurse not found");
    }

    @PostMapping("/rejectRequest/{nurseId}")
    public ResponseEntity<String> rejectRequest(@PathVariable ObjectId nurseId) {
        Optional<NurseEntity> nurse = nurseRepo.findById(nurseId);

        if (nurse.isPresent()) {
            NurseEntity updatedNurse = nurse.get();
            if (!"Pending".equals(updatedNurse.getStatus())) {
                return ResponseEntity.badRequest().body("Request expired or already handled.");
            }

            updatedNurse.setStatus("Available"); // Reset status
            nurseRepo.save(updatedNurse);

            // Trigger next nurse notification
            return ResponseEntity.ok("Request rejected. Searching for next available nurse...");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nurse not found");
    }

    @PostMapping("/completeVisit/{nurseId}")
    public ResponseEntity<String> completeVisit(@PathVariable ObjectId nurseId) {
        Optional<NurseEntity> nurse = nurseRepo.findById(nurseId);
        if (nurse.isPresent()) {
            NurseEntity updatedNurse = nurse.get();
            updatedNurse.setStatus("Available"); // Ready for next patient
            nurseRepo.save(updatedNurse);
            return ResponseEntity.ok("Visit completed. Status set to Available.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nurse not found");
    }


}
