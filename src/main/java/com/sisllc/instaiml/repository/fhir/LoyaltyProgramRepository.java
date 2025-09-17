/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.repository.fhir;

import com.sisllc.instaiml.model.fhir.LoyaltyProgram;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface LoyaltyProgramRepository extends ReactiveCrudRepository<LoyaltyProgram, String> {

    Flux<LoyaltyProgram> findByPatientId(String patientId);

    @Query("SELECT l FROM LoyaltyProgram l WHERE l.privacyConsent = true")
    Flux<LoyaltyProgram> findWithPrivacyConsent();

    Long countByProgramName(String programName);
}
