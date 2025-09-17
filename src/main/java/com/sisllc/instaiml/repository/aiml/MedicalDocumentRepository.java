/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.repository.aiml;

import com.sisllc.instaiml.model.aiml.MedicalDocument;
import java.time.OffsetDateTime;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MedicalDocumentRepository extends R2dbcRepository<MedicalDocument, String> {

    @Query("""
        SELECT id, title, text_content, specialty, document_type, publication_date,
               embeddding
        FROM medicalDocuments
        ORDER BY embeddding <=> $1
        LIMIT 3
        """)
    Flux<MedicalDocument> findSimilarDocuments(float[] embedding);

    @Query("""
        SELECT id, title, text_content, specialty, document_type, publication_date,
               embeddding
        FROM medicalDocuments
        ORDER BY embeddding <=> $1
        LIMIT $2
        """)
    Flux<MedicalDocument> findSimilarDocuments(float[] embedding, Integer limit);

    @Query("""
        INSERT INTO medicalDocuments
        (title, text_content, specialty, document_type, publication_date, embeddding)
        VALUES ($1, $2, $3, $4, $5, $6)
        RETURNING id
        """)
    Mono<Long> saveDocument(
        String title,
        String textContent,
        String specialty,
        String documentType,
        OffsetDateTime publicationDate,
        float[] embedding
    );

    @Query("""
        SELECT * FROM medical_documents
        WHERE id = $1
        FOR UPDATE SKIP LOCKED
        """)
    Mono<MedicalDocument> findByIdForUpdate(String id);

    @Query("""
        SELECT * FROM medical_documents
        WHERE id = $1
        FOR UPDATE NOWAIT
        """)
    Mono<MedicalDocument> findByIdForUpdateNoWait(String id);
}
