/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

//1. HMAC Pseudonymizer Utility
public class PHIPseudonymizer {
    //see PatientIdPseudonymizer

    private static final String HMAC_ALGO = "HmacSHA256";
    private final String secretKey;

    public PHIPseudonymizer(String secretKey) {
        this.secretKey = secretKey;
        if (secretKey == null) {
            secretKey = "secretKey";
        }
    }

    public String pseudonymize(String phiValue) {
        if (phiValue == null || phiValue.isEmpty()) {
            return "null";
        }
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(phiValue.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (IllegalStateException | InvalidKeyException | NoSuchAlgorithmException e) {
            return "ERR_HASH";
        }
    }
}
