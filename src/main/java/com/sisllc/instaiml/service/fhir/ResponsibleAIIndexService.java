/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.model.fhir.ResponsibleAIIndex;
import com.sisllc.instaiml.repository.fhir.ResponsibleAIIndexRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ResponsibleAIIndexService {

    private final ResponsibleAIIndexRepository raiRepo;

    public Flux<ResponsibleAIIndex> getAllCompliantModels() {
        return raiRepo.findByComplianceStatus("COMPLIANT");
    }

    public Mono<ResponsibleAIIndex> saveOrUpdateIndex(ResponsibleAIIndex index) {
        return raiRepo.save(index);
    }

    public Flux<ResponsibleAIIndex> getModelsByMinScore(Double minScore) {
        return raiRepo.findByMinOverallScore(minScore);
    }

    public Flux<ResponsibleAIIndex> getModelByName(String modelName) {
        return raiRepo.findByModelName(modelName);
    }

    public Mono<Double> getIndustryAverageScore() {
        return raiRepo.findAll() // Flux<ResponsibleAIIndex>
            .map(ResponsibleAIIndex::getOverallScore) // Flux<Double>
            .collect(Collectors.averagingDouble(Double::doubleValue)); // Mono<Double>
    }

    public Mono<ResponsibleAIIndex> computeAndStore(String operation, String modelName, String notes,
        double transparency, double fairness, double privacy, double regulatoryAlignment) {
        ResponsibleAIIndex r = new ResponsibleAIIndex();
        r.operation = operation;
        r.modelName = modelName;
        r.transparencyScore = clamp(transparency);
        r.fairnessScore = clamp(fairness);
        r.privacyScore = clamp(privacy);
        r.regulatoryAlignmentScore = clamp(regulatoryAlignment);
        r.notes = notes;
        return raiRepo.save(r);
    }

    private double clamp(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v)) {
            return 0.0;
        }
        if (v < 0) {
            return 0;
        }
        if (v > 1) {
            return 1;
        }
        return v;
    }

    /**
     * Example helper: derive transparency heuristics
     */
    public Map<String, Double> deriveScoresFromResponse(String prompt, String response, double modelConfidence, boolean consentPresent) {
        Map<String, Double> scores = new HashMap<>();
        // Simple heuristics — replace with real evaluators and policy rules
        double transparency = 0.6 + (response != null ? 0.2 : 0.0);
        double fairness = 0.7; // requires offline fairness eval
        double privacy = consentPresent ? 0.9 : 0.2;
        double regulatory = 0.8; // e.g., if using only approved models
        scores.put("transparency", clamp(transparency));
        scores.put("fairness", clamp(fairness));
        scores.put("privacy", clamp(privacy));
        scores.put("regulatory", clamp(regulatory));
        return scores;
    }
}
