/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util;

import java.util.List;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

public class ReactiveDatabaseCleaner {
    private final DatabaseClient databaseClient;
    private final String schemaName;

    public ReactiveDatabaseCleaner(DatabaseClient databaseClient, String schemaName) {
        this.databaseClient = databaseClient;
        this.schemaName = schemaName;
    }

    /*
    Why This Works
        Dynamic table discovery — uses information_schema.tables so it doesn’t care what tables you add/remove.
        Truncates with CASCADE — automatically clears dependent tables in the right order.
        Resets IDs — uses RESTART IDENTITY so primary key sequences start from 1 again.
        Schema-safe — only truncates the schema you specify (e.g., public or RPG_TEST).
     */

    public Mono<Void> clean() {
        // Get list of all tables in schema
        return databaseClient
            .sql("SELECT table_name FROM information_schema.tables "
                + "WHERE table_schema = $1 AND table_type = 'BASE TABLE'")
            .bind("$1", schemaName)
            .map(row -> row.get("table_name", String.class))
            .all()
            .collectList()
            .flatMapMany(tables -> {
                if (tables.isEmpty()) {
                    return Mono.empty();
                }
                // Disable constraints, truncate, re-enable
                String truncateSql = String.format(
                    "TRUNCATE TABLE %s RESTART IDENTITY CASCADE",
                    String.join(", ", qualifyTables(schemaName, tables))
                );
                return databaseClient.sql(truncateSql).fetch().rowsUpdated();
            })
            .then();
    }

    private static Iterable<String> qualifyTables(String schema, List<String> tables) {
        return tables.stream()
            .map(t -> schema + "." + t)
            .toList();
    }
}
