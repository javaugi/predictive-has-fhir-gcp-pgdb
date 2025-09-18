/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.cloud.aiplatform.v1.PredictionServiceSettings;
import com.google.protobuf.Value;
import com.google.protobuf.util.JsonFormat;
import com.sisllc.instaiml.dto.fhir.LabDataRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionDiabetesService {

    private final PredictionServiceClient client;
    private final EndpointName endpointName;

    public PredictionDiabetesService() throws IOException {
        // Initialize the Vertex AI client. Use a service account for authentication.
        GoogleCredentials credentials = GoogleCredentials.fromStream(
            // Load credentials securely from an environment variable or secret manager
            getClass().getClassLoader().getResourceAsStream("path/to/your/credentials.json")
        );
        PredictionServiceSettings settings = PredictionServiceSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build();
        this.client = PredictionServiceClient.create(settings);

        // Your specific project, location, and endpoint ID
        String projectId = "your-gcp-project";
        String location = "us-central1";
        String endpointId = "your-endpoint-id";
        this.endpointName = EndpointName.of(projectId, location, endpointId);
    }

    public Mono<Double> getRiskScore(LabDataRequest request) {
        return Mono.fromCallable(() -> {
            // Prepare the instance for prediction
            Map<String, Object> instance = new HashMap<>();
            instance.put("glucose", request.getGlucoseLevel());
            instance.put("hdl_cholesterol", request.getHdlCholesterol());

            Value.Builder instanceValue = Value.newBuilder();
            JsonFormat.parser().merge(new ObjectMapper().writeValueAsString(instance), instanceValue);

            // Make the prediction call
            PredictResponse response = client.predict(endpointName, Collections.singletonList(instanceValue.build()), Value.newBuilder().build());

            // Extract the prediction value (assuming a single numeric output)
            return response.getPredictions(0).getNumberValue();
        });
    }
}
