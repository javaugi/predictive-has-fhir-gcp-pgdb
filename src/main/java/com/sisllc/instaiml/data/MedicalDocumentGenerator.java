/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.data;

import com.sisllc.instaiml.model.aiml.MedicalDocument;
import java.time.ZoneOffset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Slf4j
public class MedicalDocumentGenerator extends DataGeneratorBase {

    public static MedicalDocument generate() {
        MedicalDocument mediDoc = MedicalDocument.builder()
            .id(UUID.randomUUID().toString())
            .version(FAKER.number().randomNumber())
            .title(FAKER.medication().drugName())
            .textContent(FAKER.medicalProcedure().icd10())
            .specialty(FAKER.doctorWho().character())
            .documentType("pdf")
            .publicationDate(FAKER.date().past(FAKER.number().numberBetween(1, 30), TimeUnit.DAYS).toInstant().atOffset(ZoneOffset.UTC))
            .embedding(getEmbeddingFloatArray())
            .pdfContent(generateFakePdfContent())
            .build();
        
        return mediDoc;
    }
    
    public static MedicalDocument generate(DatabaseClient dbClient) {
        MedicalDocument mediDoc = generate();
        log.trace("mediDoc {}", insert(dbClient, mediDoc).subscribe());
        return mediDoc;
    }
    
    public static Mono<Long> insert(DatabaseClient dbClient, MedicalDocument mediDoc) {
        return dbClient.sql("""
            INSERT INTO medications (id, version, title, text_content, specialty, documentType, publication_date, embedding, pdf_content)
                    VALUES (:id, :version, :title, :textContent, :specialty, :documentType, :publicationDate, :embedding, :pdfContent)
            """)
            .bind("id", mediDoc.getId())
            .bind("version", mediDoc.getVersion())
            .bind("title", mediDoc.getTitle())
            .bind("textContent", mediDoc.getTextContent())
            .bind("specialty", mediDoc.getSpecialty())
            .bind("documentType", mediDoc.getDocumentType())
            .bind("publicationDate", mediDoc.getPublicationDate())
            .bind("embedding", mediDoc.getEmbedding())
            .bind("pdfContent", mediDoc.getPdfContent())
            .fetch()
            .rowsUpdated();
    }        
}
