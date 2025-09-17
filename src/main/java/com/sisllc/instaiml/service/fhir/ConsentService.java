/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import org.springframework.stereotype.Service;

/**
 * Stub: check whether given consentId permits the requested operation. In
 * production connect to a consent management store.
 */
@Service
public class ConsentService {

    public boolean hasGivenConsent(String patientId, String consentId, String purpose) {
        // For demo, accept if consentId != null and not "deny"
        if (consentId == null) {
            return false;
        }
        return !"deny".equalsIgnoreCase(consentId);
    }
}
