/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import com.sisllc.instaiml.dto.fhir.FhirObservationRequest;
import com.sisllc.instaiml.service.fhir.VertexAIPredictService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class PredictionHealthRiskController {

    private final VertexAIPredictService aiService;

    public PredictionHealthRiskController(VertexAIPredictService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/risk-score")
    public ResponseEntity<Map<String, Object>> getRiskScore(@RequestBody FhirObservationRequest request) {
        // Extract features from FHIR Observation
        Map<String, Float> features = request.toFeatureMap();

        // Call AI prediction
        Float riskScore = aiService.predictRiskScore(features);

        Map<String, Object> response = new HashMap<>();
        response.put("riskScore", riskScore);
        response.put("riskLevel", riskScore > 0.7 ? "HIGH" : "LOW");

        return ResponseEntity.ok(response);
    }
}
/*
2. GCP + Spring Boot + FHIR Integration Flow
Architecture Flow:
    FHIR Data (Lab, Drug Test) → GCP BigQuery (Storage) → Vertex AI ML Model → Spring Boot REST API → Client Apps/Dashboards

Key Components
    FHIR: Standard for health data (e.g., Observation, Patient resources).
    BigQuery: Stores and processes healthcare datasets.
    Vertex AI: Runs ML models for prediction (AutoML, custom models).
    Spring Boot: Exposes REST APIs to deliver insights.

3. Example: Health Risk Score Prediction API
This example shows:
    Spring Boot REST Controller
    Integration with GCP Vertex AI for prediction (VertexAIPredictService ) - Vertex AI is a fully-managed, unified AI development platform
        for building and using generative AI. Access and utilize Vertex AI Studio, Agent Builder, and 200+ foundation models.
    Input from FHIR Observation resource

4. Next Steps for Monetization
    Package API as SaaS → Use API Gateway + Billing per request.
    Add Data Visualization → React/D3 or Looker dashboards.
    Deploy on GCP Cloud Run or GKE for scalability.
    Implement HIPAA compliance → Secure patient data using GCP DLP & IAM.
    Subscription Tiers →
        Free Tier: Limited predictions
        Pro Tier: Unlimited predictions, real-time insights
        Enterprise Tier: Dedicated analytics pipelines
 */
