package com.seniorcare.seniorCitizenAlert.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="patients")
@AllArgsConstructor
@NoArgsConstructor
public class PatientEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
}
