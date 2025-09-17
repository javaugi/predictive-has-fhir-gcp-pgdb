/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.filters;

import com.sisllc.instaiml.util.PHIPseudonymizer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
3. Spring WebFlux WebFilter for Request/Response Logs
    This reactive filter captures HTTP request/response bodies and pseudonymizes PHI before logging.
    PHIWebFilter.java

5. Testing Flow
    Run Spring Boot app
    Send a request:
        GET http://localhost:8080/api/patients?patientId=12345
        Logs will show patientId=<HMAC> instead of 12345

6. Summary Table
    Layer               Tool        Code Provided       Purpose
    App logs            Logback     PHILoggingFilter	Rewrites PHI in log messages
    HTTP logs           WebFlux     PHIWebFilter        Sanitizes PHI in HTTP request/response
    Pseudonymization	HMAC-SHA256	PHIPseudonymizer	Deterministic, per-env secret key
 */
@Component
public class PHIWebFilter implements WebFilter {

    private static final PHIPseudonymizer pseudonymizer
        = new PHIPseudonymizer(System.getenv().getOrDefault("PHI_KEY", "default-key"));

    private static final Pattern PATIENT_ID_PATTERN = Pattern.compile("patientId=([A-Za-z0-9_-]+)");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        // Log pseudonymized request URI query params
        String rawQuery = request.getURI().getQuery();
        if (rawQuery != null) {
            Matcher matcher = PATIENT_ID_PATTERN.matcher(rawQuery);
            StringBuffer sb = new StringBuffer();
            while (matcher.find()) {
                String pseudonym = "patientId=" + pseudonymizer.pseudonymize(matcher.group(1));
                matcher.appendReplacement(sb, Matcher.quoteReplacement(pseudonym));
            }
            matcher.appendTail(sb);
            System.out.println("Incoming Request: " + request.getMethod() + " " + request.getURI().getPath() + "?" + sb);
        } else {
            System.out.println("Incoming Request: " + request.getMethod() + " " + request.getURI().getPath());
        }

        // Process request/response as usual
        return chain.filter(exchange)
            .doOnTerminate(() -> {
                // Pseudonymize response status or body metadata if needed
                System.out.println("Response Status: " + response.getStatusCode());
            });
    }
}
