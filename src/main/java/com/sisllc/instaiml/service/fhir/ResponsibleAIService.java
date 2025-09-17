/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.dto.fhir.PatientInquiryRequest;
import com.sisllc.instaiml.dto.fhir.PatientInquiryResponse;
import org.springframework.stereotype.Service;

/**
 * A service for tracking and enforcing Responsible AI principles.
 */
public interface ResponsibleAIService {

    void performPrivacyCheck(PatientInquiryRequest request);

    void logAiInteraction(PatientInquiryRequest request, PatientInquiryResponse response);

    void updateResponsibleAiIndex(PatientInquiryResponse response);
}
