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
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table("loyaltyPrograms")
public class LoyaltyProgram {
    @Id
    private String id;

    @Column("patient_id")
    private String patientId;

    @Column("program_name")
    private String programName;

    @Column("enrollment_date")
    private OffsetDateTime enrollmentDate;

    @Builder.Default
    @Column("points_balance")
    private Integer pointsBalance = 0;

    @Builder.Default
    @Column("tier_level")
    private String tierLevel = "BRONZE";

    @Column("personalized_recommendations")
    private String personalizedRecommendations;

    @Column("privacy_consent")
    private Boolean privacyConsent;

    @Column("data_usage_preferences")
    private String dataUsagePreferences;

    @CreatedDate
    @Column("created_date")
    private OffsetDateTime createdDate;

    @LastModifiedDate
    @Column("updated_date")
    private OffsetDateTime updatedDate;

    public LoyaltyProgram(String patientId, String programName) {
        this.patientId = patientId;
        this.programName = programName;
        this.enrollmentDate = OffsetDateTime.now();
    }

}
