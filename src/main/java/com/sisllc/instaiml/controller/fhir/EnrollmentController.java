/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.fhir;

import com.sisllc.instaiml.dto.fhir.EnrollmentRequest;
import com.sisllc.instaiml.dto.fhir.EnrollmentResponse;
import com.sisllc.instaiml.service.fhir.EnrollmentService;
import com.sisllc.instaiml.service.fhir.EnrollmentService.EnrollmentResult;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aiapi/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponse> enrollPatient(@Valid @RequestBody EnrollmentRequest req) {
        EnrollmentResult r = service.enrollPatient(req.getPatientId(), req.getConsentId(), req.getEmail(), req.isAcceptMarketing(), req.getFreeText());

        EnrollmentResponse resp = EnrollmentResponse.builder().enrollmentId(r.success ? r.idOrMessage : null)
            .success(r.success).message(r.success ? "Enrolled" : r.idOrMessage).confidenceScore(r.confidence)
            .explanation(r.explanation).build();
        return ResponseEntity.ok(resp);
    }
}
