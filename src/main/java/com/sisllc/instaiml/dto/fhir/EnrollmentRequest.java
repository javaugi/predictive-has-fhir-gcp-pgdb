/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {
    @NotBlank
    private String patientId;   // internal id (not PII)
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String consentId;   // reference to a consent record
    private boolean acceptMarketing;
    private String freeText;    // free text input from patient to be used for personalization
}
