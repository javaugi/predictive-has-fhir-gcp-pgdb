/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.util.PIIUtils;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * Demo: you should instead write to secure audit logs (WORM), include
 * requestor, IP, ID tokens, etc.
 */
@Service
public class AuditService {

    public void recordPrompt(String operation, String patientId, String prompt, String response, String modelName, String provenance) {
        // Redact PII in prompt/response when logging
        String redactedPrompt = PIIUtils.redactPII(prompt);
        String redactedResponse = PIIUtils.redactPII(response);

        // In production, write to secure audit store with proper access control & retention policies
        System.out.printf("[%s] AUDIT %s patient=%s model=%s prov=%s prompt=%s response=%s%n",
            Instant.now(), operation, patientId, modelName, provenance, redactedPrompt, redactedResponse);
    }
}
