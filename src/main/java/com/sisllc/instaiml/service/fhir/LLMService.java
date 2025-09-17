/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.dto.fhir.PatientInquiryRequest;
import com.sisllc.instaiml.dto.fhir.PatientInquiryResponse;
import org.springframework.stereotype.Service;

/**
 * A mock service interface for interacting with a Generative AI model. In a
 * real application, this would integrate with a model API like Google's Gemini.
 */
public interface LLMService {

    PatientInquiryResponse getPersonalizedResponse(PatientInquiryRequest request);
}
