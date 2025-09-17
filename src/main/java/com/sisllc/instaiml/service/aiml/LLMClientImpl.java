/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.aiml;

//import static jakarta.persistence.GenerationType.UUID;
import com.sisllc.instaiml.dto.fhir.LLMResponse;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class LLMClientImpl implements LLMClient {

    @Override
    public LLMResponse generate(String prompt, Map<String, Object> providerMetadata) {
        // IMPORTANT: This is a mock. Replace with an HTTP client to the LLM provider.
        String modelName = "demo-model";
        String modelVersion = "0.1";
        String result = "Suggested enrollment message: Welcome! Based on your preferences we'll ...";
        double confidence = 0.75;
        String provenance = UUID.randomUUID().toString();
        return new LLMResponse(result, confidence, modelName, modelVersion, provenance);
    }

}
