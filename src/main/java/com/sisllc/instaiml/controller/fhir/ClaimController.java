/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import com.sisllc.instaiml.dto.fhir.ProcessResult;
import com.sisllc.instaiml.model.fhir.Claim;
import com.sisllc.instaiml.model.types.ProviderStats;
import com.sisllc.instaiml.service.fhir.ClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping
    public Mono<ResponseEntity<Claim>> ingest(@Valid @RequestBody Claim claim) {
        return claimService.ingestClaim(claim)
            .mapNotNull(e -> ResponseEntity.status(HttpStatus.CREATED).body(e))
            //.map(e -> ResponseEntity.status(HttpStatus.CREATED).body(e))
            .onErrorResume(e -> {
            log.debug("Error ingest claim {} {}", claim, e.getMessage());
            return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
            });
    }

    @PostMapping("/process")
    public ResponseEntity<ProcessResult> processClaim(@RequestBody Claim claim) {
        return ResponseEntity.ok(claimService.processClaim(claim));
    }

    @GetMapping("/stats/{providerId}")
    public ResponseEntity<ProviderStats> getProviderStats(@PathVariable String providerId) {
        return ResponseEntity.ok(claimService.getProviderStatistics(providerId));
    }
}
