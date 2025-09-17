package com.sisllc.instaiml.model.fhir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import com.sisllc.instaiml.model.types.*;


@Data
@Builder(toBuilder = true)
@Entity
@Table("claims")
public class Claim {

    @Id
    private String id;

    @Column("external_id")
    private String externalId; // external system claim id => supports idempotency

    @Column("external_id")
    private String patientId;

    @Column("external_id")
    private OffsetDateTime claimDate;

    @Column("external_id")
    private String encounterId; // optional: ties professional & facility claims
    @Column("received_date")
    private OffsetDateTime receivedDate;

    @Column("review_date")
    private OffsetDateTime reviewDate;

    @Column("status")
    private ClaimStatus status;

    @Column("pre_pay")
    private boolean prePay;
    private boolean outpatient;

    @Column("claim_type")
    private ClaimType claimType;
    @Column("review_type")
    private ReviewType reviewType;
    @Column("processing_stage")
    private ProcessingStage processingStage;

    @Column("physician_submit_time")
    private OffsetDateTime physicianSubmitTime;
    @Column("hospital_submit_time")
    private OffsetDateTime hospitalSubmitTime;
    @Column("service_date")
    private OffsetDateTime serviceDate;
    @Column("Submit_time")
    private OffsetDateTime SubmitTime;

    @Column("billed_amount")
    private Double billedAmount;
}
