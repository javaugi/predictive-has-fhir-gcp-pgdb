/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.repository.fhir;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import com.sisllc.instaiml.model.fhir.*;
import java.time.OffsetDateTime;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClaimRepository extends ReactiveCrudRepository<Claim, String> {

    public Mono<Claim> findByExternalId(String externalId);

    @Query("select c from Claim c where c.patientId = :patientId and c.claimType = :claimType "
        + "and c.claimDate between :from and :to")
    public Flux<Claim> findByPatientAndTypeBetween(String patientId, String claimType, OffsetDateTime from, OffsetDateTime to);

    public Flux<Claim> findByEncounterId(String encounterId);
}
