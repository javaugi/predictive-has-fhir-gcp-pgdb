/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.model.fhir;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import java.time.LocalDateTime;
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
@Table("responsibleAIIndexes")
public class ResponsibleAIIndex {
    @Id
    public String id;

    public String operation; // e.g. "LoyaltyEnrollment"

    @Column("model_name")
    public String modelName;

    @Column("transparency_score")
    public double transparencyScore;

    @Column("fairness_score")
    public double fairnessScore;

    @Column("created_date")
    public double privacyScore;

    @Column("regulatory_alignment_score")
    public double regulatoryAlignmentScore;

    @Column("overall_score")
    private Double overallScore;

    @Column("compliance_status")
    private String complianceStatus;

    public String notes; // JSON or text explaining scoring decisions

    @CreatedDate
    @Column("created_date")
    private OffsetDateTime createdDate;

    @LastModifiedDate
    @Column("updated_date")
    private OffsetDateTime updatedDate;

    public Double calculateOverallScore() {
        return (transparencyScore + fairnessScore + privacyScore + regulatoryAlignmentScore) / 4;
    }

    public String calculateComplianceStatus() {
        return overallScore >= 8.0 ? "COMPLIANT" : "NEEDS_REVIEW";
    }

    public void setTransparencyScore(Double transparencyScore) {
        this.transparencyScore = transparencyScore;
        this.overallScore = calculateOverallScore();
        this.complianceStatus = calculateComplianceStatus();
    }

    public void setFairnessScore(Double fairnessScore) {
        this.fairnessScore = fairnessScore;
        this.overallScore = calculateOverallScore();
        this.complianceStatus = calculateComplianceStatus();
    }

    public void setRegulatoryAlignmentScore(Double regulatoryAlignmentScore) {
        this.regulatoryAlignmentScore = regulatoryAlignmentScore;
        this.overallScore = calculateOverallScore();
        this.complianceStatus = calculateComplianceStatus();
    }
}
