/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "ollama")
@Component
public class OllamaProperties {
    private String baseUrl = "http://localhost:11434";
    private String model = "llama3"; // or "mistral", "gemma", etc.
    private String embeddingModel = "llama3";
    private Double temperature = 0.7;
    private String apiUrl = "http://localhost:11434/api/generate";
    private Integer connTimeoutMillis = 5000;
    private Integer respTimeoutMillis = 10000;
}
