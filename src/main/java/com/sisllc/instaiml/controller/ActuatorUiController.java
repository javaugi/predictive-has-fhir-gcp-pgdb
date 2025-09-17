/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.controller;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/actuator-ui")
public class ActuatorUiController {

    private final WebClient webClient = WebClient.create("http://localhost:8088");

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> allEndpoints() {
        return webClient.get()
            .uri("/actuator")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/health", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> health() {
        return webClient.get()
            .uri("/actuator/health")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/info", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> info() {
        return webClient.get()
            .uri("/actuator/info")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/metrics", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> metrics() {
        return webClient.get()
            .uri("/actuator/metrics")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/env", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> env() {
        return webClient.get()
            .uri("/actuator/env")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/beans", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> beans() {
        return webClient.get()
            .uri("/actuator/beans")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/loggers", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> loggers() {
        return webClient.get()
            .uri("/actuator/loggers")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }

    @GetMapping(value = "/prometheus", produces = MediaType.TEXT_HTML_VALUE)
    public Mono<String> prometheus() {
        return webClient.get()
            .uri("/actuator/prometheus")
            .retrieve()
            .bodyToMono(Map.class)
            .map(body -> "<html><body><h1>Health</h1><pre>" + body + "</pre></body></html>");
    }
}
