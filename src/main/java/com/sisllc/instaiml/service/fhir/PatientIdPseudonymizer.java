/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

//1. HMAC Pseudonymizer Utility
public class PatientIdPseudonymizer {
    //PHIPseudonymizer

    /*
3. Key Points

Per-Environment Key:
    For dev, test, prod, use different keys (rotate periodically).
    Store in Azure KeyVault, AWS Secrets Manager, or HashiCorp Vault.

HMAC-SHA256 ensures:
    Deterministic mapping → Same input → Same pseudonym
    Non-reversible outside your environment without the key
    Base64 URL-safe encoding → No special characters in output
     */

    private final String secretKey; // Per-environment key, e.g., loaded from Vault or KMS

    public PatientIdPseudonymizer(String secretKey) {
        this.secretKey = secretKey;
    }

    public String pseudonymize(String patientId) {
        try {
            // Create HMAC-SHA256 instance
            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSha256.init(keySpec);

            // Compute HMAC digest
            byte[] hashBytes = hmacSha256.doFinal(patientId.getBytes(StandardCharsets.UTF_8));

            // Return Base64 or hex string
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error pseudonymizing patient ID", e);
        }
    }

    public static void main(String[] args) {
        // Example: key should come from secure config (e.g., Spring Boot application.yml or Azure KeyVault)
        String envKey = "MY_ENV_SPECIFIC_SECRET_KEY_2025";

        PatientIdPseudonymizer pseudonymizer = new PatientIdPseudonymizer(envKey);

        String patientId = "patient-12345";
        String pseudonym = pseudonymizer.pseudonymize(patientId);

        System.out.println("Original ID: " + patientId);
        System.out.println("Pseudonymized ID: " + pseudonym);
    }
}
