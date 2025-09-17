/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.springframework.stereotype.Service;

@Service
public class FhirValidationService {

    /*
Severity levels:
    INFORMATION
    WARNING
    ERROR
    FATAL

5) Summary
    Default validation → DefaultProfileValidationSupport + InMemoryTerminologyServerValidationSupport
    Custom profile validation → PrePopulatedValidationSupport for loading StructureDefinitions
    Works for Patient, Observation, Bundle, etc.
     */
    //Basic Validator Setup
    public boolean validate() throws Exception {
        // Create FHIR context for R4
        FhirContext ctx = FhirContext.forR4();

        // Create validator
        FhirValidator validator = ctx.newValidator();

        // Add instance validator module
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(
            new ValidationSupportChain(
                new DefaultProfileValidationSupport(ctx), // Default HL7 profiles
                new InMemoryTerminologyServerValidationSupport(ctx) // Terminology validation
            )
        );

        validator.registerValidatorModule(instanceValidator);

        // Example: Create a FHIR Patient resource
        Patient patient = new Patient();
        patient.addName().setFamily("Smith").addGiven("John");
        patient.setId("Patient/123");

        // Validate resource
        ValidationResult result = validator.validateWithResult(patient);

        // Print results
        if (result.isSuccessful()) {
            System.out.println("Validation passed!");
        } else {
            result.getMessages().forEach(next -> {
                System.out.println(next.getSeverity() + " - " + next.getLocationString() + " - " + next.getMessage());
            });
            return false;
        }

        return true;
    }

    //Validate Against a Custom FHIR Profile
    /*
    Here:
        We manually load the StructureDefinition.
        The patient resource references the profile in its meta.profile.
     */
    public boolean validateOnProfile() throws Exception {
        // Create FHIR context for R4
        FhirContext ctx = FhirContext.forR4();

        // Create validator
        FhirValidator validator = ctx.newValidator();

        // Add instance validator module
        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(
            new ValidationSupportChain(
                new DefaultProfileValidationSupport(ctx), // Default HL7 profiles
                new InMemoryTerminologyServerValidationSupport(ctx) // Terminology validation
            )
        );

        validator.registerValidatorModule(instanceValidator);

        // Example: Create a FHIR Patient resource
        Patient patient = new Patient();
        patient.addName().setFamily("Smith").addGiven("John");
        patient.setId("Patient/123");

        // Validate resource
        ValidationResult result = validator.validateWithResult(patient);

        // Print results
        if (result.isSuccessful()) {
            System.out.println("Validation passed!");
        } else {
            result.getMessages().forEach(next -> {
                System.out.println(next.getSeverity() + " - " + next.getLocationString() + " - " + next.getMessage());
            });
        }

        PrePopulatedValidationSupport prePopulated = new PrePopulatedValidationSupport(ctx);

        // Load custom StructureDefinition
        String profileJson = Files.readString(Paths.get("src/main/resources/jsonschemas/patient-profile.json"));
        StructureDefinition myProfile = (StructureDefinition) ctx.newJsonParser().parseResource(profileJson);
        prePopulated.addStructureDefinition(myProfile);

        ValidationSupportChain chain = new ValidationSupportChain(
            prePopulated,
            new DefaultProfileValidationSupport(ctx),
            new InMemoryTerminologyServerValidationSupport(ctx)
        );

        //FhirInstanceValidator instanceValidator = new FhirInstanceValidator(chain);
        //validator.registerValidatorModule(instanceValidator);
        // Validate patient against the custom profile
        patient.getMeta().addProfile("http://example.org/fhir/StructureDefinition/MyPatientProfile");
        ValidationResult customResult = validator.validateWithResult(patient);

        //4) Handling Validation Results
        if (!customResult.isSuccessful()) {
            customResult.getMessages().forEach(issue -> {
                System.err.println(
                    "Severity: " + issue.getSeverity()
                    + " | Location: " + issue.getLocationString()
                    + " | Message: " + issue.getMessage()
                );
            });
            return false;
        }

        return true;
    }
}
