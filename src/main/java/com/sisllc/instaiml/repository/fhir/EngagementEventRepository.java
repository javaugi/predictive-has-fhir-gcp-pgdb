package com.sisllc.instaiml.repository.fhir;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
import com.sisllc.instaiml.model.fhir.PatientEngagementEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface EngagementEventRepository extends ReactiveCrudRepository<PatientEngagementEvent, String> {

    Flux<PatientEngagementEvent> findByPatientId(String patientId);
}
