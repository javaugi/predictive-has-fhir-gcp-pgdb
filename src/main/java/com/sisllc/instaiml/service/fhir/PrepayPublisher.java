/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.model.fhir.Claim;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PrepayPublisher {

    public void publishPrepayEvent(Claim trigger, List<Claim> matches) {
        // In production: write to Kafka topic (compact key: patientId) with evidence + audit id.
        System.out.println("PREPAY EVENT: trigger=" + trigger.getExternalId() + " matches=" + matches.size());
    }
}
