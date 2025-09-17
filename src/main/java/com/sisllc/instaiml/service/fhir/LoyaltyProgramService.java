/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.service.fhir;

import com.sisllc.instaiml.model.fhir.LoyaltyProgram;
import com.sisllc.instaiml.repository.fhir.LoyaltyProgramRepository;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LoyaltyProgramService {

    @Autowired
    private LoyaltyProgramRepository loyaltyProgramRepo;

    public Mono<LoyaltyProgram> enrollPatient(LoyaltyProgram program) {
        return loyaltyProgramRepo.save(program);
    }

    public Flux<LoyaltyProgram> getPatientPrograms(String patientId) {
        return loyaltyProgramRepo.findByPatientId(patientId);
    }

    /*
    Why this works
        findById() → gives Mono<LoyaltyProgram>
        filter() → keeps only if matches programName
        switchIfEmpty() → throws error if no matching record found
        flatMap() → updates and saves if found
        This is the most idiomatic Spring WebFlux + Reactive way.
     */
    public Mono<LoyaltyProgram> updatePointsById(String id, String programName, Integer points) {
        return loyaltyProgramRepo.findById(id)
            .filter(Objects::nonNull)
            .filter(p -> p.getProgramName().equals(programName))
            .switchIfEmpty(Mono.error(new RuntimeException("Loyalty Program not found")))
            .flatMap(program -> {
                program.setPointsBalance(program.getPointsBalance() + points);
                program.setUpdatedDate(OffsetDateTime.now());
                return loyaltyProgramRepo.save(program);
            });
    }

    public Flux<LoyaltyProgram> updatePointsByPatientId(String patientId, String programName, Integer points) {
        return loyaltyProgramRepo.findByPatientId(patientId)
            .filter(Objects::nonNull)
            .switchIfEmpty(Flux.error(new RuntimeException("Loyalty Program not found")))
            .filter(p -> p.getProgramName().equals(programName))
            .flatMap(program -> {
                program.setPointsBalance(program.getPointsBalance() + points);
                program.setUpdatedDate(OffsetDateTime.now());
                return loyaltyProgramRepo.save(program);
            });
    }


    public Long getProgramEnrollmentCount(String programName) {
        return loyaltyProgramRepo.countByProgramName(programName);
    }
}
