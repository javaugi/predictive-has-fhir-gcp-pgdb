/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.repository.fhir;

import com.sisllc.instaiml.model.fhir.ResponsibleAIIndex;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ResponsibleAIIndexRepository extends ReactiveCrudRepository<ResponsibleAIIndex, String> {

    Flux<ResponsibleAIIndex> findByComplianceStatus(String complianceStatus);

    @Query("SELECT r FROM ResponsibleAIIndex r WHERE r.overallScore >= :minScore")
    Flux<ResponsibleAIIndex> findByMinOverallScore(Double minScore);

    Flux<ResponsibleAIIndex> findByModelName(String modelName);
}
