package com.seniorcare.seniorCitizenAlert.controller;

import com.seniorcare.seniorCitizenAlert.entity.NurseEntity;
import com.seniorcare.seniorCitizenAlert.entity.PatientEntity;
import com.seniorcare.seniorCitizenAlert.repository.PatientRepo;
import com.seniorcare.seniorCitizenAlert.service.NurseService;
import com.seniorcare.seniorCitizenAlert.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Service
@RequestMapping("/patient")
@RestController
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private PatientRepo patientRepo;
    @GetMapping("/get-health")
    public String healthcheck()
    {
        return "OK";
    }

    @GetMapping("/get-all")
    public List<PatientEntity> getAllPatient()
    {
        List<PatientEntity> all = patientService.getAll();
        return all;
    }
    @PostMapping("/register")
    public PatientEntity createPatient(@RequestBody PatientEntity person)
    {
        return patientService.saveNewPatient(person);
    }
    @PostMapping("/alert")
    public ResponseEntity<?> alertNearestNurse(@RequestBody PatientEntity patient) {
        List<NurseEntity> availableNurses = nurseService.getNearestAvailableNurses(patient);
        if (availableNurses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available nurses at the moment.");
        }
        return nurseService.processNurseAssignment(patient, availableNurses, 0);
    }
}
