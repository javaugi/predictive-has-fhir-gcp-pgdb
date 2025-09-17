/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

// package com.health.analytics.api
import com.sisllc.instaiml.service.fhir.LoyaltyAnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/analytics")
public class LoyaltyAnalyticsController {

    private final LoyaltyAnalyticsService service;

    public LoyaltyAnalyticsController(LoyaltyAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/loyalty-score/{patientId}")
    public Mono<Map<String, Object>> getLoyaltyScore(@PathVariable String patientId) {
        return service.computeLoyaltyScore(patientId);
    }
}
