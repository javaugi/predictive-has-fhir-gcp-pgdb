/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import com.sisllc.instaiml.dto.fhir.ClinicalTextRequest;
import com.sisllc.instaiml.service.fhir.NLPService;
import com.sisllc.instaiml.util.converters.FHIRConverter;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/nlp")
public class NLPController {

    @Autowired
    private NLPService nlpService;

    @PostMapping("/process")
    public Mono<Bundle> processText(@RequestBody ClinicalTextRequest request) {
        return nlpService.processText(request.getText())
            .map(FHIRConverter::toFHIRBundle);
    }
}
/*
7. Sample Request

POST http://localhost:8080/api/nlp/process

{
  "text": "Patient John Doe, ID 12345, diagnosed with Diabetes on 2025-09-14. Prescribed Metformin 500mg daily."
}

8. Sample FHIR JSON Response
{
  "resourceType": "Bundle",
  "type": "collection",
  "entry": [
    {
      "resource": {
        "resourceType": "Patient",
        "id": "12345",
        "identifier": [
          {
            "system": "http://hospital.org/patients",
            "value": "12345"
          }
        ],
        "name": [
          {
            "family": "Doe",
            "given": ["John"]
          }
        ]
      }
    },
    {
      "resource": {
        "resourceType": "Condition",
        "id": "cond-12345",
        "subject": { "reference": "Patient/12345" },
        "code": {
          "coding": [
            {
              "system": "http://snomed.info/sct",
              "code": "44054006",
              "display": "Diabetes"
            }
          ]
        },
        "recordedDate": "2025-09-14"
      }
    }
  ]
}


This gives you:
    Reactive WebFlux pipeline
    Mock NLP extraction → Replace with real AI later
    FHIR-compliant output ready for EHR or analytics
*/
