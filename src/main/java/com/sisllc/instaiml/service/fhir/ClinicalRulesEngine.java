/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.dto.fhir.ReviewResult;
import com.sisllc.instaiml.dto.fhir.ValidationResult;
import com.sisllc.instaiml.model.fhir.Claim;
import org.springframework.stereotype.Service;

@Service
public class ClinicalRulesEngine {

    public ValidationResult validate(Claim claim) {
        return ValidationResult.builder().build();
    }

    public boolean isCrossClaimEligible(Claim claim) {
        return true;
    }

    public ReviewResult applyRules(Claim claim) {
        return ReviewResult.builder().build();
    }
}
