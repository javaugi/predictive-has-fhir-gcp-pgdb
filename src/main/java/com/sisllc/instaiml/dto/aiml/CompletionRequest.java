/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.aiml;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CompletionRequest {

    private String prompt;
    private Integer maxTokens;
    private Double temperature;
    private Double frequencyPenalty;
    private Double presencePenalty;
    private Double topP;
    private Integer bestOf;
    private Boolean stream;
    private List<String> stop;

}