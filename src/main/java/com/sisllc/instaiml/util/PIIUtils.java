/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util;

import java.util.regex.Pattern;

/**
 *
 * @author javau
 */
public class PIIUtils {
// Very simple patterns — replace with enterprise PII detection or libraries

    private static final Pattern EMAIL = Pattern.compile("\\b[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}\\b");
    private static final Pattern PHONE = Pattern.compile("\\b\\+?\\d[\\d\\-\\s()]{6,}\\b");

    public static String redactPII(String input) {
        if (input == null) {
            return null;
        }
        String s = EMAIL.matcher(input).replaceAll("[REDACTED_EMAIL]");
        s = PHONE.matcher(s).replaceAll("[REDACTED_PHONE]");
        // additional PII rules: SSN, DOB, addresses...
        return s;
    }

    public static String hashPII(String value) {
        if (value == null) {
            return null;
        }
        // example simple hash; use a keyed hash (HMAC) and salt in production
        return Integer.toHexString(value.hashCode());
    }
}
