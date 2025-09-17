/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a single metric tracked in the Responsible AI Index.
 */
@Data
@Builder(toBuilder = true)
public class AiIndexMetric {

    private String metricName;
    private double score;
    private String explanation;

    public AiIndexMetric(String metricName, double score, String explanation) {
        this.metricName = metricName;
        this.score = score;
        this.explanation = explanation;
    }

}
