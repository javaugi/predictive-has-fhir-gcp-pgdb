/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReactiveDbCleanerByClient {
    @Autowired
    private DatabaseClient databaseClient;

    private List<String> tableNames;

    @BeforeAll
    public void init() {
        // Fetch all non-system tables in public schema
        tableNames = databaseClient.sql("""
            SELECT tablename
            FROM pg_tables
            WHERE schemaname = 'public'
        """)
            .map(row -> row.get("tablename", String.class))
            .all()
            .collectList()
            .block();
    }

    @AfterEach
    public void cleanDatabase() {
        if (tableNames == null || tableNames.isEmpty()) {
            return;
        }

        Flux.fromIterable(tableNames)
            .flatMap(table -> databaseClient.sql("TRUNCATE TABLE " + table + " RESTART IDENTITY CASCADE").fetch().rowsUpdated())
            .then()
            .block();
    }
}
