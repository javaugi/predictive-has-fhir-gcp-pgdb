/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import com.sisllc.instaiml.model.fhir.ResponsibleAIIndex;
import com.sisllc.instaiml.service.fhir.ResponsibleAIIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/aiapi/ai-governance")
public class AIGovernanceController {

    @Autowired
    private ResponsibleAIIndexService aiService;

    @PostMapping("/index")
    public Mono<ResponseEntity<ResponsibleAIIndex>> createAIIndex(@RequestBody ResponsibleAIIndex index) {
        return aiService.saveOrUpdateIndex(index)
            .mapNotNull(s -> ResponseEntity.status(HttpStatus.CREATED).body(s))
            .onErrorResume(e -> {
            log.error("Error createAIIndex {}" + e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
            });
    }

    @GetMapping("/compliant-models")
    public Flux<ResponseEntity<ResponsibleAIIndex>> getCompliantModels() {
        return aiService.getAllCompliantModels()
            .mapNotNull(e -> ResponseEntity.status(HttpStatus.FOUND).body(e))
            .onErrorResume(e -> {
            log.error("Error getCompliantModels {}" + e.getMessage());
            return Flux.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            });
    }

    @GetMapping("/industry-average")
    public Mono<ResponseEntity<Double>> getIndustryAverage() {
        return aiService.getIndustryAverageScore()
            .mapNotNull(e -> ResponseEntity.status(HttpStatus.FOUND).body(e))
            .onErrorResume(e -> {
                log.error("Error getIndustryAverage {}" + e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            });
    }

    @GetMapping("/model/{modelName}")
    public Flux<ResponseEntity<ResponsibleAIIndex>> getModelDetails(@PathVariable String modelName) {
        return aiService.getModelByName(modelName)
            .mapNotNull(e -> ResponseEntity.status(HttpStatus.FOUND).body(e))
            .onErrorResume(e -> {
                log.error("Error getModelDetails model {} ", modelName, e.getMessage());
            return Flux.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
            });

    }
}
