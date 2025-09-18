/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;
import java.util.HashMap;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class FhirObservationRequest {
    private float cholesterol;
    private float bloodPressure;
    private float glucose;

    public Map<String, Float> toFeatureMap() {
        Map<String, Float> map = new HashMap<>();
        map.put("cholesterol", cholesterol);
        map.put("bloodPressure", bloodPressure);
        map.put("glucose", glucose);
        return map;
    }
}
