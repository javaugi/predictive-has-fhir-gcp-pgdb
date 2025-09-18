/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PredictionNoShowsService {
    private static final String GCP_HEALTHCARE_API_URL = "https://healthcare.googleapis.com/v1/projects/{project}/locations/{location}/datasets/{dataset}/fhirStores/{fhirStore}/fhir";
    private static final String GCP_HC_VERTEX_AI_URL = "https://{your-region}-aiplatform.googleapis.com/v1/projects/{project}/locations/{your-region}/endpoints/{your-endpoint-id}:predict";

    public Mono<String> predictNoShows(WebClient webClient, String accessToken, String department, String startDate, String endDate) {
        try {
            // 1. Fetch and aggregate historical appointment data from FHIR store
            // This is a complex query in practice. Here's a simplified conceptual call.
            String fhirQuery = String.format("/Appointment?date=ge%s&date=le%s", startDate, endDate);
            // ... and other params for status & department

            Mono<JsonNode> fhirData = webClient.get()
                .uri(GCP_HEALTHCARE_API_URL + fhirQuery)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class);

            return fhirData.flatMap(data -> {
                // 2. Transform FHIR Bundle resource into features for the ML model
                Map<String, Object> requestInstance = new HashMap<>();
                // Feature engineering happens here (e.g., day of week, history of no-shows, weather, etc.)
                requestInstance.put("department", department);
                requestInstance.put("day_of_week", LocalDate.parse(startDate).getDayOfWeek().toString());
                // ... add more features

                // 3. Create the payload for Vertex AI prediction
                Map<String, Object> vertexRequest = new HashMap<>();
                vertexRequest.put("instances", new Object[]{requestInstance});

                // 4. Call the deployed model on Vertex AI
                return webClient.post()
                    .uri(GCP_HC_VERTEX_AI_URL)
                    .header("Authorization", "Bearer " + accessToken)
                    .bodyValue(vertexRequest)
                    .retrieve()
                    .bodyToMono(String.class); // This will contain the prediction
            });

        } catch (Exception e) {
            return Mono.just("Error: " + e.getMessage());
        }
    }

}
