/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClaimEvent(
    String claimId,
    String patientId,
    String providerId,
    ClaimType claimType,
    BigDecimal amount,
    LocalDateTime serviceDate,
    ClaimStatus status,
    String failureReason
    ) {

    public enum ClaimType {
        PHYSICIAN, HOSPITAL, OUTPATIENT
    }

    public enum ClaimStatus {
        SUBMITTED, VALIDATED, APPROVED, REJECTED, PROCESSED
    }
}
