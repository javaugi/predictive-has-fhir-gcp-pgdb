package com.sisllc.instaiml.model.fhir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patientEvents")
public class PatientEvent implements java.io.Serializable {

    @Id
    String id;

    @Column("patient_id")
    String patientId;
    @Column("event_type")
    String eventType;

    String payload;

    @CreatedDate
    @Column("created_date")
    private OffsetDateTime createdDate;
}
