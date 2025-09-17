/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.model.fhir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "patientEngagementEvents")
public class PatientEngagementEvent {

    @Id
    private String id;

    @Column("patient_id")
    private String patientId;
    @Column("event_type")
    private String eventType;  // e.g., APPOINTMENT, CLAIM, LAB_RESULT

    private String metadata;   // JSON string with additional details

    @CreatedDate
    @Column("created_date")
    private OffsetDateTime createdDate;
}
