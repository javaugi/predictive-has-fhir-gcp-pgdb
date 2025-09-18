/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.google.auth.oauth2.GoogleCredentials;
import com.sisllc.instaiml.service.fhir.PredictionNoShowsService;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
@RestController
@RequestMapping("/api/predictions")
public class PredictionNoShowsController {

    private final WebClient webClient;
    private final String accessToken;
    private final PredictionNoShowsService predictionNoShowsService;

    @Value("${gcp_healthcare_api_base_url}")
    private String gcpHealthcareApiBaseUrl;
    @Value("${gcp_auth_api_url}")
    private String gcpAuthApiUrl;

    public PredictionNoShowsController(PredictionNoShowsService predictionNoShowsService) {
        this.predictionNoShowsService = predictionNoShowsService;
        // This requires the GOOGLE_APPLICATION_CREDENTIALS environment variable to be set.
        this.webClient = WebClient.builder()
            .baseUrl(gcpHealthcareApiBaseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
        this.accessToken = getAccessToken();
    }

    private String getAccessToken() {
        try {
            GoogleCredentials credentials = GoogleCredentials.getApplicationDefault()
                .createScoped(gcpAuthApiUrl);
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException ex) {
            log.error("Error getAccessToken", ex);
        }
        return null;
    }

    @GetMapping("/no-shows")
    public Mono<String> predictNoShows(@RequestParam String department, @RequestParam String startDate,
        @RequestParam String endDate) {
        return predictionNoShowsService.predictNoShows(webClient, accessToken, department, startDate, endDate);
    }
}

/*
Java Code Example: Predictive Analytics Service
Let's create a simplified example for Strategy #1 (Predicting Patient No-Shows). This is a conceptual Spring Boot service that uses the
    GCP Healthcare API to access FHIR data and Vertex AI for a prediction.

1. High-Level Architecture
    A Spring Boot application receives a request to predict no-shows for a given date range and department.
    The application queries de-identified FHIR data from a GCP Healthcare API FHIR store via its REST API.
    It aggregates and transforms this data into a format suitable for the ML model.
    It calls a deployed Vertex AI Endpoint to get the prediction.
    It returns the prediction result.

Next Steps and Recommendations
    Start with a Pilot: Choose one high-value, well-defined use case (e.g., no-show prediction). Build a minimal viable product (MVP) and
        partner with a single hospital or insurer to validate it.
    Invest in Data Engineering: The quality of your features (feature engineering) is more important than the complexity of your model. Building
        robust, automated pipelines to clean, harmonize (using FHIR), and featurize your data is 80% of the work.
    Leverage GCP's Managed Services: Use BigQuery for your data warehouse and Vertex AI Pipelines (Kubeflow) to create reproducible ML workflows.
        This manages scalability and reduces DevOps overhead.
    Security First: Design your system with a "zero-trust" security model from day one. Use GCP's IAM roles diligently and encrypt everything.
 */
