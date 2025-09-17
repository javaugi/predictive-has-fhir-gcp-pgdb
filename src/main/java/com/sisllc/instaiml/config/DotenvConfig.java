/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@Configuration
@PropertySource(value = "file:.env", ignoreResourceNotFound = true) // This doesn't load it as env vars
public class DotenvConfig {

    static {
        try {
            Dotenv dotenv = Dotenv.load();
            dotenv.entries().forEach(entry
                -> log.debug("DotenvConfig .env key {} value {}", entry.getKey(), entry.getValue())
            );
        } catch (Exception e) {
            // Handle cases where .env file is not found
        }
    }
}
