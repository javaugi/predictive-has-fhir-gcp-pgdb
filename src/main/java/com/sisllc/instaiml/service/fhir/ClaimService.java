/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.dto.fhir.ProcessResult;
import com.sisllc.instaiml.model.types.ClaimStatus;
import com.sisllc.instaiml.model.types.ClaimType;
import com.sisllc.instaiml.model.types.ProviderStats;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.sisllc.instaiml.model.fhir.*;
import com.sisllc.instaiml.repository.fhir.*;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ClaimService {

    private final ClaimRepository claimRepo;
    private final PrepayPublisher publisher; // simple interface to "emit"

    /*
     * Ingest claim; idempotent by externalId.
     */
    @Transactional
    public Mono<Claim> ingestClaim(Claim incoming) {
        // 1. Check if the claim already exists using Mono.flatMap
        return claimRepo.findByExternalId(incoming.getExternalId())
            .switchIfEmpty(Mono.defer(() -> {
                // 2. If no existing claim is found (switchIfEmpty), save the new one
                return claimRepo.save(incoming)
                    .flatMap(saved -> {
                    if (null == saved.getClaimType()) {
                        return Mono.just(saved); // return the saved claim for other types
                    } else // 3. Chain the detection logic after saving the new claim
                    {
                        switch (saved.getClaimType()) {
                            case PROFESSIONAL -> {
                                // Search for facility claims
                                OffsetDateTime from = saved.getClaimDate();
                                OffsetDateTime to = saved.getClaimDate().plusDays(30);
                                return claimRepo.findByPatientAndTypeBetween(saved.getPatientId(), ClaimType.FACILITY.toString(), from, to)
                                    .collectList()
                                    .doOnNext(matches -> {
                                        if (!matches.isEmpty()) {
                                            publisher.publishPrepayEvent(saved, matches);
                                        }
                                    })
                                    .thenReturn(saved); // return the saved claim
                            }
                            case FACILITY -> {
                                // Find prior professional claims
                                OffsetDateTime from = saved.getClaimDate().minusDays(30);
                                OffsetDateTime to = saved.getClaimDate();
                                return claimRepo.findByPatientAndTypeBetween(saved.getPatientId(), ClaimType.PROFESSIONAL.toString(), from, to)
                                    .collectList()
                                    .doOnNext(matches -> {
                                        if (!matches.isEmpty()) {
                                            publisher.publishPrepayEvent(saved, matches);
                                        }
                                    })
                                    .thenReturn(saved); // return the saved claim
                            }
                            default -> {
                                return Mono.just(saved); // return the saved claim for other types
                            }
                        }
                    }
                });
            }))
            .map(claim -> claim); // The method must return a Mono<Claim>
    }

    public ProcessResult processClaim(Claim claim) {
        return ProcessResult.builder().build();
    }

    public ProviderStats getProviderStatistics(String providerId) {
        return ProviderStats.builder().build();
    }

    // Using Stream API for claim processing
    public Map<ClaimStatus, List<Claim>> processClaims(List<Claim> claims) {
        return claims.stream()
            .filter(claim -> claim.getReviewDate() != null)
            .peek(this::applyClinicalRules)
            .collect(Collectors.groupingBy(Claim::getStatus));
    }

    // Parallel processing for performance
    public List<Claim> processClaimsParallel(List<Claim> claims) {
        return claims.parallelStream()
            .filter(claim -> claim.isPrePay() || claim.isOutpatient())
            .map(this::enrichClaimData)
            .collect(Collectors.toList());
    }

    private void applyClinicalRules(Claim claim) {
        // Clinical validation logic
        if (isCrossClaimEligible(claim)) {
            claim.setStatus(ClaimStatus.CCCR_ELIGIBLE);
        }
    }

    private Claim enrichClaimData(Claim claim) {
        // Data enrichment logic
        return claim;
    }

    private boolean isCrossClaimEligible(Claim claim) {
        // Business logic for CCCR eligibility
        return claim.getPhysicianSubmitTime().isBefore(claim.getHospitalSubmitTime());
    }
}
