package com.sisllc.instaiml;

import java.util.Arrays;
import static org.junit.jupiter.api.AssertionsKt.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

//@ExtendWith(SpringExtension.class)
//@WebFluxTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import({WebClientAutoConfiguration.class, TestPostgresConfig.class})
@ActiveProfiles("TEST")
public class MyReactiveApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired(required = false)
    private ApplicationContext context;

    @Test
    public void contextLoads() {
        assertNotNull(context);
        Arrays.stream(context.getBeanDefinitionNames())
              .sorted()
              .forEach(System.out::println);
    }

    @Test
    void contextLoadsHealth() {
        // Verify that the Spring application context has loaded successfully.
        // If the database auto-configuration was still an issue, this would fail.
        assertNotNull(context, "Application context should not be null.");

        // Optional: Print all loaded bean names for debugging/verification
        System.out.println("Beans loaded in context for MyReactiveApplicationTests:");
        Arrays.stream(context.getBeanDefinitionNames())
            .sorted()
            .forEach(System.out::println);

        // Example: Test a simple endpoint to ensure the web layer is functioning
        webTestClient.get().uri("/api/health")
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("Hello, Reactive Spring Boot!");
    }
}
