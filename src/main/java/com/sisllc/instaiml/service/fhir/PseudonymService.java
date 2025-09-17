/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PseudonymService {

    private final PatientIdPseudonymizer pseudonymizer;

    public PseudonymService(@Value("${spring.security.pseudonym-key}") String key) {
        if (key == null) {
            key = "secretKey";
        }
        this.pseudonymizer = new PatientIdPseudonymizer(key);
    }

    public String getPseudonym(String patientId) {
        //5. Optional: Adding a Salt or Context
        String contextSpecificPseudonym = pseudonymizer.pseudonymize(patientId + "|modelA");
        return pseudonymizer.pseudonymize(patientId);
    }
}
