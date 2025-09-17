/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LLMResponse {
    private String text;
    private double confidence; // heuristic; many providers don't directly give this
    private String modelName;
    private String modelVersion;
    private String provenance; // e.g., prompt id or trace id
    private String explanation;       // human-readable explanation of decisions

    public LLMResponse(String text, double confidence, String modelName, String modelVersion, String provenance) {
        this.text = text;
        this.confidence = confidence;
        this.modelName = modelName;
        this.modelVersion = modelVersion;
        this.provenance = provenance;
    }
}
