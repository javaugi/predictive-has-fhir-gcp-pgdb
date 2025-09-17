/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sisllc.instaiml.repository.fhir;

import com.sisllc.instaiml.model.fhir.PatientEvent;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientEventRepository extends ReactiveCrudRepository<PatientEvent, String> {

}
