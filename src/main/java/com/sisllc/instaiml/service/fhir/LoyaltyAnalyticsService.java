/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

// package com.health.analytics.service
import com.sisllc.instaiml.model.fhir.PatientEngagementEvent;
import com.sisllc.instaiml.repository.fhir.EngagementEventRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LoyaltyAnalyticsService {

    private final EngagementEventRepository engagementEventRepo;

    public LoyaltyAnalyticsService(EngagementEventRepository repo) {
        this.engagementEventRepo = repo;
    }

    public Mono<Map<String, Object>> computeLoyaltyScore(String patientId) {
        // Chain reactive operators to transform the Flux into a Mono
        return engagementEventRepo.findByPatientId(patientId)
            .count()
            .map(count -> count * 10)
            .map(score -> {
                // Build the result map within the reactive stream
                Map<String, Object> responsibleAI = new HashMap<>();
                responsibleAI.put("transparency", 0.95);
                responsibleAI.put("fairness", 0.90);
                responsibleAI.put("regulatoryCompliance", true);

                Map<String, Object> result = new HashMap<>();
                result.put("patientId", patientId);
                result.put("loyaltyScore", score);
                result.put("responsibleAIIndex", responsibleAI);

                return result;
            });
    }
}
