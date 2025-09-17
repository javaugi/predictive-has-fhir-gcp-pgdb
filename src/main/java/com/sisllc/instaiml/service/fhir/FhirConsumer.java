/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.sisllc.instaiml.model.fhir.PatientEvent;
import com.sisllc.instaiml.repository.fhir.PatientEventRepository;
import org.hl7.fhir.r4.model.Claim;
import org.hl7.fhir.r4.model.Encounter;

import java.time.Instant;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class FhirConsumer {

    private final PatientEventRepository patientEventRepo;
    private final KafkaTemplate<String, String> kafka;
    private final FhirValidationService fhirValidationService;
    private final PseudonymService pseudonymService;

    private final FhirContext fhirContext = FhirContext.forR4();

    @KafkaListener(topics = "raw-fhir-events", groupId = "fhir-normalizer")
    public void onMessage(String fhirJson) {
        IParser parser = fhirContext.newJsonParser();
        IBaseResource res = parser.parseResource(fhirJson);
        String type = res.fhirType();//.getResourceType().name();

        boolean isValid = fhirValidation(fhirJson);
        if (!isValid) {
            log.warn("fhirValidation failed");
        }

        PatientEvent e = new PatientEvent();
        switch (res) {
            case Claim c -> {
                // Example: map claim->patientId using insured party or patient reference
                e.setPatientId(extractPatientIdFromClaim(c));
                e.setEventType("CLAIM");
                e.setPayload(fhirJson);
                e.setCreatedDate(OffsetDateTime.now());
            }
            case Encounter en -> {
                e.setPatientId(extractPatientIdFromEncounter(en));
                e.setEventType("ENCOUNTER");
                e.setPayload(fhirJson);
                e.setCreatedDate(OffsetDateTime.now());
            }
            default -> {
                e.setPatientId("unknown");
                e.setEventType(type);
                e.setPayload(fhirJson);
                e.setCreatedDate(OffsetDateTime.now());
            }
        }
        // Persist normalized event for analytics
        patientEventRepo.save(e);

        //Scoring microservice or scoring-results to dashboards, billing automation, clinician UI
        kafka.send("patient-events ", type, fhirJson);
    }

    private boolean fhirValidation(String fhirJson) {
        IParser parser = fhirContext.newJsonParser();
        IBaseResource res = parser.parseResource(fhirJson);

        try {
            // Minimal validation: resource type allowed?
            String resourceType = res.fhirType();//.getResourceType().name();
            boolean isValid = fhirValidationService.validate();
        } catch (Exception ex) {

        }

        return true;
    }

    private String extractPatientIdFromClaim(Claim c) {
        if (c.hasPatient() && c.getPatient().hasReference()) {
            return pseudonymize(c.getPatient().getReference());
        }
        // fallback: insured party or provider; production: canonical normalization
        return "unknown";
    }

    private String extractPatientIdFromEncounter(Encounter en) {
        if (en.hasSubject() && en.getSubject().hasReference()) {
            return pseudonymize(en.getSubject().getReference());
        }
        return "unknown";
    }

    private String pseudonymize(String patientId) {
        String externalSafeId = pseudonymService.getPseudonym(patientId);
        return externalSafeId;
        //externalModelClient.sendData(externalSafeId, otherData);
    }
}
