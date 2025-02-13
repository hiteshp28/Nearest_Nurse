package com.seniorcare.seniorCitizenAlert.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="nurses")
@AllArgsConstructor
@NoArgsConstructor
public class NurseEntity {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;
    private String phone;
    private double latitude;
    private double longitude;
    private boolean onDuty;
}
