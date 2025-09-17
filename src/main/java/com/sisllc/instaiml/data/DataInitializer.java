/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.data;

import com.sisllc.instaiml.config.DatabaseProperties;
import com.sisllc.instaiml.service.InsurancePricingAnalyticalService;
import com.sisllc.instaiml.service.aiml.MedicalDocumentRagService;
import com.sisllc.instaiml.service.aiml.OllamaRagMediDocService;
import com.sisllc.instaiml.util.ReactiveDatabaseCleaner;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataInitializer implements ApplicationRunner{
    private static final String TRUNC_TABLE = "TRUNCATE TABLE %s CASCADE";
    private static final String MED_DOC_RAG_Q = "Why are the pancreatic and esophageal cancers so hard to detect early?";
    
    private final DatabaseProperties dbProps;
    private final DataGeneratorService dataGenService;
    private final InsurancePricingAnalyticalService anylyticalService;
    private final MedicalDocumentRagService ragService;
    private final OllamaRagMediDocService ollamaRagService;
    private final DatabaseClient dbClient;

    private String database;
    
    @Override
    public void run(ApplicationArguments args) {
        this.database = dbProps.getDatabase();
        log.info("DataInitializer Spring Boot {} database {} dbClient {}", SpringBootVersion.getVersion(), this.database, dbClient);

        if (dbProps.getTruncateMockData()) {
            truncateData();
        }
        
        log.info("createTables ... ");  
        createTables();
        log.info("Done createTables ");  

        if (dbProps.getSetupMockUserOnly()) {
            dataGenService.seedDataUserOnly();
        } else {
            dataGenService.seedData();
        }
        log.info("Done seedData ");  
        anylyticalService.performAnalytics();
        log.info("Done performAnalytics ");
        ragService.answerMedicalQuestion(MED_DOC_RAG_Q)
            .flatMap(a -> {
                log.info("MedicalDocumentRagService.answerMedicalQuestion {}", a);
                return Mono.just(a);
            })
            .doOnSuccess(a -> {
                log.info("Success MedicalDocumentRagService.answerMedicalQuestion doOnSuccess {}", a);
            })
            .switchIfEmpty(Mono.empty()
                .doOnNext(dummy -> log.info("Empty MedicalDocumentRagService.answerMedicalQuestion{}", dummy))
                .then(Mono.empty())
            )
            .doOnError(e -> log.error("Error MedicalDocumentRagService.answerMedicalQuestion {}", e.getMessage()));

        log.info("Done MedicalDocumentRagService answerMedicalQuestion");
        ollamaRagService.answerMedicalQuestion(MED_DOC_RAG_Q)
            .flatMap(a -> {
                log.info("OllamaRagMediDocService.answerMedicalQuestion {}", a);
                return Mono.just(a);
            })
            .doOnSuccess(a -> {
                log.info("Success OllamaRagMediDocService.answerMedicalQuestion doOnSuccess {}", a);
            })
            .switchIfEmpty(Mono.empty()
                .doOnNext(dummy -> log.info("Empty OllamaRagMediDocService.answerMedicalQuestion {}", dummy))
                .then(Mono.empty())
            )
            .doOnError(e -> log.error("Error OllamaRagMediDocService.answerMedicalQuestion {}", e.getMessage()));
        log.info("Done all DataInitializer Spring Boot {} database {}", SpringBootVersion.getVersion(), this.database);
    }

    protected void truncateData() {
        log.info("truncateData ... ");
        new ReactiveDatabaseCleaner(dbClient, "public").clean();
        log.info("truncateData Done ");
    }
    
    protected void createTables() {

        for (String table : DDL_TABLES) {
            checkTableExists(table)
                .flatMap(exists -> {
                    if (exists) {
                        //truncateDataWhenNeeded(table);
                    } else {
                        String ddlSql = getDdlSql(table);
                        if (!ddlSql.isEmpty()) {
                            addTableBySql(ddlSql);
                        }
                    }
                    return Mono.empty();
                })
                .subscribe();
        }
    }

    protected void truncateDataWhenNeeded(String table) {
        checkPgTableDataExists(table)
            .flatMap(hasData -> {
                if (hasData && dbProps.getTruncateMockData()) {
                    String ddlSql = String.format(TRUNC_TABLE, table);
                    if (!ddlSql.isEmpty()) {
                        truncateTableBySql(ddlSql);
                    }
                }
                return Mono.empty();
            }).subscribe();
    }

    private void truncateTableBySql(String sql) {
        //dbClient.sql(sql).then().block();
        dbClient.sql(sql).then().subscribe();
    }
    
    private String getDdlSql(String table) {        
        StringBuilder sb = new StringBuilder();
        try{
            InputStream inputStream = getClass().getResourceAsStream(dbProps.getDdlSchemaDir() + table + ".sql");
            sb.append(new String(inputStream.readAllBytes()));
        } catch(IOException ex) {
            log.error("Error getDdlSql table {}", table, ex);
        }
        
        return sb.toString();
    }
    
    private void addTableBySql(String sql) {
        dbClient.sql(sql)
            .then().doOnError(e -> {
                log.error("Error addTableBySql {}", sql, e);
            })
            .block(Duration.ofSeconds(10)); // Blocking here is okay for initialization
        //dbClient.sql(sql).then().subscribe();
    }
    
    private Mono<Boolean> checkTableExists(String tableName) {
        return checkPgTableExists(tableName);
    }
    
    private Mono<Boolean> checkPgTableExists(String tableName) {
        return dbClient.sql("""
            SELECT EXISTS (
                SELECT FROM information_schema.tables 
                WHERE table_name = $1
            )
            """)
            .bind(0, tableName.toLowerCase())
            .map(row -> row.get(0, Boolean.class))
            .one().defaultIfEmpty(false);
    }

    /*
    Potential Issues in the following Code
        Table name parameterization: You can't use bind parameters for table names in most databases. Table names must be part of the SQL string.
        Potential SQL injection: If you directly interpolate the table name, you need to validate it first
     */
    private Mono<Boolean> checkPgTableDataExists(String tableName) {
        return dbClient.sql("SELECT EXISTS (SELECT 1 FROM " + tableName.toLowerCase() + " LIMIT 1)")
            .map(row -> row.get(0, Boolean.class))
            .first()
            .defaultIfEmpty(false)
            .doOnError(e -> log.error("Error checking data existence for {} \n {}", tableName, e.getMessage()));
    }

    public static final List<String> DDL_TABLES = List.of("users", "patients", "medications", "physicians", "pharmacies",
        "drugInventories", "prescriptions", "insuranceCompanies", "insuranceProviders", "insurancePlans", "members",
        "patientMembers", "planPricings", "coverageDetails", "geographicPricings", "claimsData", "medicalDocuments");
}
