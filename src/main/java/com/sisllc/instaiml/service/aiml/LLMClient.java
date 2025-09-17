/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.aiml;

import com.sisllc.instaiml.dto.fhir.LLMResponse;
import java.util.Map;

/**
 *
 * @author javau
 */
public interface LLMClient {
    /**
     * Ask the LLM a prompt and return a structured response. providerMetadata
     * should capture model name, version, temperature, promptTokens, etc.
     */
    LLMResponse generate(String prompt, Map<String, Object> providerMetadata);
}
