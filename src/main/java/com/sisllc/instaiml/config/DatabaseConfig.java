/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.config;

import com.sisllc.instaiml.config.DatabaseProperties.ProfileSetting;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

@Slf4j
@Configuration
public class DatabaseConfig extends AbstractR2dbcConfiguration {
    @Autowired
    protected DatabaseProperties dbProps;
    @Autowired
    private Environment env;
    
    private ProfileSetting profileSetting;
    
    @PostConstruct
    public void init() {
        log.info("DatabaseConfig profiles {}", Arrays.toString(env.getActiveProfiles()));
        if (env.getActiveProfiles() == null || env.getActiveProfiles().length == 0
            || env.acceptsProfiles(Profiles.of(ProfileTestConfig.TEST_PROFILE))) {
            profileSetting = ProfileSetting.TEST;
        } else if (env.acceptsProfiles(Profiles.of(ProfileProdConfig.PROD_PROFILE))) {
           profileSetting = ProfileSetting.PROD;
        } else {
            profileSetting = ProfileSetting.DEV;
        }
        
        dbProps.setupBaseDbProps(profileSetting);
        log.info("DatabaseConfig props {}", dbProps);
    }

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        /*
        PostgresqlConnectionFactory baseFactory = new PostgresqlConnectionFactory(
            PostgresqlConnectionConfiguration.builder()
                .host(dbProps.getHost())
                .port(Integer.parseInt(dbProps.getPort()))
                .database(dbProps.getDatabase())
                .username(dbProps.getUsername())
                .password(dbProps.getPassword())
                .build()
        );

        return new ConnectionPool(ConnectionPoolConfiguration.builder(baseFactory)
            .initialSize(dbProps.getPoolInitialSize())
            .maxSize(dbProps.getPoolMaxSize())
            .build()
        );
        // */
        return ConnectionFactories.get(ConnectionFactoryOptions
            .parse(dbProps.getUrl())
            .mutate()
            .option(ConnectionFactoryOptions.USER, dbProps.getUsername())
            .option(ConnectionFactoryOptions.PASSWORD, dbProps.getPassword())
            .build());
    }
    
    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }

    @Bean
    public DatabaseClient dbClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.create(connectionFactory);
    }

    /*
    Transaction Best Practices - see MedicalDocumentService
        1. Keep transactions short - Especially with pessimistic locks
        2. Use appropriate isolation levels - Configure in @Transactional
        3. Implement retry logic - For optimistic locking scenarios
        4. Monitor for deadlocks - Set up proper logging
        5. Consider alternative approaches - Like event sourcing for high-contention scenarios
        6. Example of setting isolation level:
            @Transactional(isolation = Isolation.SERIALIZABLE)
            public Mono<Void> highIsolationOperation() {
                // Your transactional code
            }
     */
    @Bean
    public ReactiveTransactionManager reactiveTransactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager txManager) {
        return TransactionalOperator.create(txManager);
    }

}
