/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class LoyaltyScore {
    public String patientId;
    public double score;
    public String modelId;
    public double confidence;
    public String explanation;
}
