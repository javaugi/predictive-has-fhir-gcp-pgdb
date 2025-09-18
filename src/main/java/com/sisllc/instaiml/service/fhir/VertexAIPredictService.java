/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.google.cloud.aiplatform.v1.EndpointName;
import com.google.cloud.aiplatform.v1.PredictRequest;
import com.google.cloud.aiplatform.v1.PredictResponse;
import com.google.cloud.aiplatform.v1.PredictionServiceClient;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class VertexAIPredictService {

    private static final String PROJECT_ID = "your-gcp-project";
    private static final String ENDPOINT_ID = "vertex-endpoint-id";
    private static final String LOCATION = "us-central1";

    public Float predictRiskScore(Map<String, Float> features) {
        try (PredictionServiceClient predictionServiceClient = PredictionServiceClient.create()) {
            EndpointName endpointName = EndpointName.of(PROJECT_ID, LOCATION, ENDPOINT_ID);

            // Build a Struct where each field value is a protobuf Value (number)
            Struct.Builder structBuilder = Struct.newBuilder();
            for (Map.Entry<String, Float> e : features.entrySet()) {
                structBuilder.putFields(
                    e.getKey(),
                    Value.newBuilder().setNumberValue(e.getValue().doubleValue()).build()
                );
            }

            // Wrap struct in a Value as the "instance"
            Value instance = Value.newBuilder().setStructValue(structBuilder.build()).build();

            PredictRequest predictRequest = PredictRequest.newBuilder()
                .setEndpoint(endpointName.toString())
                .addInstances(instance)
                .build();

            PredictResponse predictResponse = predictionServiceClient.predict(predictRequest);

            // Parse response (common case: response[0] is a struct with field "score")
            Value p0 = predictResponse.getPredictions(0);
            double score = 0.0;
            if (p0.hasStructValue() && p0.getStructValue().getFieldsMap().containsKey("score")) {
                score = p0.getStructValue().getFieldsOrThrow("score").getNumberValue();
            } else if (p0.getKindCase() == Value.KindCase.NUMBER_VALUE) {
                score = p0.getNumberValue();
            } else {
                // fallback: try first field if it's a struct
                if (p0.hasStructValue() && !p0.getStructValue().getFieldsMap().isEmpty()) {
                    score = p0.getStructValue().getFieldsMap().values().iterator().next().getNumberValue();
                }
            }

            return (float) score;
        } catch (Exception ex) {
            throw new RuntimeException("Prediction failed", ex);
        }
    }

}
