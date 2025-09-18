/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import com.sisllc.instaiml.dto.fhir.LabDataRequest;
import com.sisllc.instaiml.service.fhir.PredictionDiabetesService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PredictionDiabetesController {

    private final PredictionDiabetesService predictionService;

    public PredictionDiabetesController(PredictionDiabetesService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/api/v1/predict/risk-score")
    public Mono<ResponseEntity<Map<String, Object>>> predictRiskScore(@RequestBody LabDataRequest request) {
        return predictionService.getRiskScore(request)
            .map(score -> {
                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("patientId", request.getPatientId());
                responseBody.put("riskScore", score);
                return ResponseEntity.status(HttpStatus.OK).body(responseBody);
            })
            .onErrorResume(e -> {
                // Handle potential errors like invalid input or AI model issues
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage())));
            });
    }
}
