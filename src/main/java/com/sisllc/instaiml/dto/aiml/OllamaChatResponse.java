/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.aiml;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class OllamaChatResponse {

    private OllamaMessage message;
}
