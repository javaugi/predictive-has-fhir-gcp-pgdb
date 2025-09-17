/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sisllc.instaiml.repository.fhir.*;
import com.sisllc.instaiml.dto.fhir.*;
import com.sisllc.instaiml.model.fhir.*;
import com.sisllc.instaiml.model.types.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ClaimProcessingService {

    private final ClaimRepository claimRepository;
    private final ClinicalRulesEngine rulesEngine;
    private final ApplicationEventPublisher eventPublisher;

    public ProcessResult processClaim(Claim claim) {
        // Validate clinical data
        ValidationResult validation = rulesEngine.validate(claim);

        if (!validation.isValid()) {
            return ProcessResult.builder().failed("Clinical validation failed").build();
        }

        // Check for cross-claim opportunities
        boolean isCrossClaim = rulesEngine.isCrossClaimEligible(claim);

        if (isCrossClaim) {
            applyCCCRRules(claim);
        }

        claimRepository.save(claim);
        return ProcessResult.builder().claim(claim).build();
    }

    public ReviewResult reviewClaim(Claim claim) {
        // 1. Complex processing logic
        ReviewResult result = rulesEngine.applyRules(claim);

        // 2. Save the core result to DB
        claimRepository.save(claim);

        // 3. Fire an async event for non-critical side-effects (auditing)
        eventPublisher.publishEvent(new ClaimReviewedEvent(this, claim, result));

        return result; // Return quickly to the caller
    }

    private void applyCCCRRules(Claim claim) {
        // Complex business logic for cross-claim review
        claim.setReviewType(ReviewType.CCCR);
        claim.setProcessingStage(ProcessingStage.PREPAY);
    }
}
