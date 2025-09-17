/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.converters;

import com.sisllc.instaiml.dto.fhir.ExtractedData;
import org.hl7.fhir.r4.model.*;

public class FHIRConverter {

    public static Bundle toFHIRBundle(ExtractedData data) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);

        // Patient Resource
        Patient patient = new Patient();
        patient.setId(data.getPatientId());
        patient.addIdentifier().setSystem("http://hospital.org/patients").setValue(data.getPatientId());
        patient.addName().setFamily("Doe").addGiven("John");

        // Condition Resource
        Condition condition = new Condition();
        condition.setId("cond-" + data.getPatientId());
        condition.setSubject(new Reference("Patient/" + data.getPatientId()));
        condition.getCode().addCoding()
            .setSystem("http://snomed.info/sct")
            .setCode("44054006") // SNOMED code for Diabetes mellitus
            .setDisplay(data.getDiagnosis());
        condition.setRecordedDateElement(new DateTimeType(data.getDiagnosisDate()));

        // Add resources to Bundle
        bundle.addEntry().setResource(patient);
        bundle.addEntry().setResource(condition);

        return bundle;
    }
}
