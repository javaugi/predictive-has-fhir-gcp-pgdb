/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.repository;

import com.sisllc.instaiml.TestPostgresConfig;
import com.sisllc.instaiml.data.UserGenerator;
import com.sisllc.instaiml.model.User;
import com.sisllc.instaiml.util.ReactiveDatabaseCleaner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

@Slf4j
@SpringBootTest
@Import(TestPostgresConfig.class)
@Disabled("Temporarily disabled for CICD")
public class RepositoryCleanupTest {
    private static final String FAKE_USERNAME = new net.datafaker.Faker().internet().username();
    private static ReactiveDatabaseCleaner dbCleaner;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void setUp(@Autowired DatabaseClient databaseClient) {
        dbCleaner = new ReactiveDatabaseCleaner(databaseClient, "public"); // Change schema if needed
        dbCleaner.clean().block(); // Initial cleanup
        log.info("Done setUp cleanup FAKE_USERNAME {}", FAKE_USERNAME);
    }

    @Test
    void saveAndRetrieveUser() {
        User user = UserGenerator.generate(FAKE_USERNAME);
        user.setId(null);

        userRepository.save(user)
            .as(StepVerifier::create)
            .expectNextMatches(savedUser -> savedUser.getId() != null)
            .verifyComplete();

        userRepository.findAll()
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }

    @AfterEach
    void cleanUpAfterEach() {
        dbCleaner.clean().block();
        log.info("Done cleanUpAfterEach");
    }
}
