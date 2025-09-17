/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

// @SpringBootTest loads the entire application context.
// webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT starts a real server on a random port.
// @ActiveProfiles("mock") will apply the configurations from application-mock.properties,
// disabling the problematic Cloud SQL R2DBC auto-configuration.
//@ExtendWith(SpringExtension.class)
//@WebFluxTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import({WebClientAutoConfiguration.class, TestPostgresConfig.class})
@ActiveProfiles("TEST")
public class HealthCheckTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }

    @Test
    void shouldReturnHealthStatusUp() {
        // Test the Spring Boot Actuator health endpoint
        webTestClient.get()
            .uri("/actuator/health")
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.status").isEqualTo("UP");
    }
}

/*
Common Actuator Endpoints
    Endpoint                Description
    /actuator/health        Shows health info
    /actuator/info          Shows info from application.properties
    /actuator/metrics       Metrics like JVM, CPU, DB, HTTP reqs
    /actuator/env           Shows environment variables and props
    /actuator/beans         Shows all Spring beans
    /actuator/loggers       Change log levels at runtime
    /actuator/prometheus    App health details and system gauge from Prometheus

Summary
    @SpringBootApplication + Actuator exposes useful diagnostics /actuator/health gives system health (DB, disk, custom indicators)
    Extend with custom health indicators
    Integrates with Prometheus, Grafana, Azure Monitor, etc.
 */
/*
org.springdoc/springdoc-openapi-starter-webmvc-ui
# default: /v3/api-docs
springdoc.api-docs.path=/api-docs
springdoc.api-docs.swagger-ui.path=/swagger-ui.html

This gives you:
    /v3/api-docs            → JSON OpenAPI JSON    spec
    /swagger-ui.html        → Swagger UI
    /swagger-ui/index.html  → alternative Swagger UI path
    /v3/api-docs.yaml       -> Bonus: You can also generate a YAML spec
 */
