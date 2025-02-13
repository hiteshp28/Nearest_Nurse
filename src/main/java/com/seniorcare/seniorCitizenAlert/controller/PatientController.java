package com.seniorcare.seniorCitizenAlert.controller;

import org.springframework.web.bind.annotation.*;


@RequestMapping("/patient")
@RestController
public class PatientController {
    @GetMapping("/get-health")
    public String healthcheck()
    {
        return "OK";
    }

    @GetMapping
    public void getAllPatient()
    {

    }

    @GetMapping
    public void getPatientbyId()
    {

    }

    @PostMapping
    public void createPatient()
    {

    }

    @PutMapping
    public void editPtientDetails()
    {

    }
}
