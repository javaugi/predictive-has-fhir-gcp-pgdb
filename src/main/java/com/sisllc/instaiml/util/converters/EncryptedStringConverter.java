/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
// This is a simplistic example. Use a strong, standard algorithm from your Cloud KMS or a library.

@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    // In reality, use a proper encryptor from Jasypt or Google Cloud KMS/AWS KMS
    // @Autowired
    // private StringEncryptor encryptor;
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) {
            return null;
        }
        // return encryptor.encrypt(attribute); // Real implementation
        return "ENCRYPTED_" + attribute; // Placeholder logic
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        // return encryptor.decrypt(dbData); // Real implementation
        return dbData.replace("ENCRYPTED_", ""); // Placeholder logic
    }
}
