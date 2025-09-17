/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.dto.fhir;

import lombok.Builder;
import lombok.Data;
import com.sisllc.instaiml.model.fhir.*;

@Data
@Builder(toBuilder = true)
public class ProcessResult {
    String failed;
    Claim claim;

}
