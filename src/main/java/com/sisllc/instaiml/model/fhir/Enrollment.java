/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.model.fhir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table("enrollments")
public class Enrollment {
    @Id
    private String id;

    @Column("patient_id")
    private String patientId;
    @Column("email_hash")
    private String emailHash; // hashed pseudo-identifier to avoid storing raw email
    @Column("marketing_accepted")
    private boolean marketingAccepted;

    @CreatedDate
    @Column("created_date")
    private OffsetDateTime createdDate;

    @LastModifiedDate
    @Column("updated_date")
    private OffsetDateTime updatedDate;
}
