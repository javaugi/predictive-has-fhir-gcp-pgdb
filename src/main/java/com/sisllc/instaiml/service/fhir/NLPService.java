/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.dto.fhir.ExtractedData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class NLPService {

    public Mono<ExtractedData> processText(String text) {
        // Mock extraction: look for simple patterns
        ExtractedData data = new ExtractedData();
        data.setPatientId("12345");
        data.setDiagnosis("Diabetes");
        data.setDiagnosisDate("2025-09-14");
        data.setMedication("Metformin 500mg");
        return Mono.just(data);
    }
}
