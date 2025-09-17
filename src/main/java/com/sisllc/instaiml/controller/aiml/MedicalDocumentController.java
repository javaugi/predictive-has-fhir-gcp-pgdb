/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller.aiml;

import com.sisllc.instaiml.model.aiml.MedicalDocument;
import com.sisllc.instaiml.service.aiml.MedicalDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/medidoc")
@RequiredArgsConstructor
public class MedicalDocumentController {

    private final MedicalDocumentService mediDocService;

    @GetMapping
    public Flux<MedicalDocument> finalAll() {
        return mediDocService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<MedicalDocument> findById(String id) {
        return mediDocService.findById(id);
    }
}
