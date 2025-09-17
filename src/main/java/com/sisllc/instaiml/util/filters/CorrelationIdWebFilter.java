/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.filters;

import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorrelationIdWebFilter implements WebFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    /*
        see logback-spring-ex2.xml
            Now logs will include:
            2025-09-06 10:12:31 [af23b17c-92a1-4df7-b9ef-239d1e4c123a] INFO  c.mypackage.MyService - Processing request


        Step D: Filter and Group Logs in Kibana/Splunk
            Logs now include X-Correlation-Id automatically.
            In ELK/EFK, create dashboards grouped by X-Correlation-Id.
            Helps you trace requests across multiple services.
     */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }

        String finalCorrelationId = correlationId;
        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, correlationId);

        return chain.filter(exchange)
            .contextWrite(ctx -> ctx.put(CORRELATION_ID_HEADER, finalCorrelationId))
            .doFinally(signalType -> MDC.clear());
    }
}


/*
In org.slf4j.MDC, MDC stands for Mapped Diagnostic Context.
It is a feature used in logging frameworks like SLF4J (Simple Logging Facade for Java) to enrich log messages with contextual information. This
    context is typically stored on a per-thread basis, allowing you to associate specific data (like a user ID, session ID, or transaction ID)
    with all log messages generated within that thread's execution. This is particularly useful in multi-threaded environments, such as web applications
    or microservices, where logs from different operations can be interleaved, making it difficult to trace a single request or process. By using MDC,
    you can easily filter, group, and analyze log entries based on this contextual data.
 */
 /*
1. Why Correlation ID + MDC
    Correlation ID: A unique ID per request, passed along between services via HTTP headers (e.g., X-Correlation-Id).
    MDC: Stores contextual info (like correlation ID, user ID) in thread-local storage for logging frameworks (e.g., Logback, Log4j2).
    Benefit: All logs for the same request share the same correlation ID → easier tracing and debugging.
2. Steps to Implement
    Step A: Generate or propagate correlation ID
    (1) Create a OncePerRequestFilter for Spring MVC (or WebFilter for Reactive).
    (2) add logback-spring-ex2.xml
        Now logs will include:
        2025-09-06 10:12:31 [af23b17c-92a1-4df7-b9ef-239d1e4c123a] INFO  c.mypackage.MyService - Processing request

Step C: Pass Correlation ID Between Microservices

When one service calls another:

    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Correlation-Id", MDC.get("X-Correlation-Id"));

    HttpEntity<String> entity = new HttpEntity<>(null, headers);
    restTemplate.exchange("http://other-service/api/data", HttpMethod.GET, entity, String.class);


For WebClient in reactive:
    WebClient webClient = WebClient.builder().build();
    webClient.get()
        .uri("http://other-service/api/data")
        .header("X-Correlation-Id", MDC.get("X-Correlation-Id"))
        .retrieve()
        .bodyToMono(String.class);

Step D: Filter and Group Logs in Kibana/Splunk
    Logs now include X-Correlation-Id automatically.
    In ELK/EFK, create dashboards grouped by X-Correlation-Id.
    Helps you trace requests across multiple services.

3. End-to-End Flow
    Service A receives request → generates correlation ID if not present.
    Service A logs with correlation ID → calls Service B with same correlation ID header.
    Service B logs with same correlation ID.
    All logs for that request share the same correlation ID → traceable in logging tools.
 */
/*
2. Logging with JSON + Correlation IDs

Use SLF4J + Logback for structured logs.

Add logback-spring.xml in src/main/resources
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder class="net.logstash.logback.encoder.LoggingEventCompositeJsonEncoder">
            <providers>
                <timestamp/>
                <logLevel/>
                <threadName/>
                <loggerName/>
                <message/>
                <mdc/>
                <context/>
            </providers>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>

Add Correlation ID Filter using this class
 */
