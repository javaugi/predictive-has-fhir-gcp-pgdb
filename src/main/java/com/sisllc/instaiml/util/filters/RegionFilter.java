/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sisllc.instaiml.util.filters;

import com.sisllc.instaiml.util.RegionContextHolder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Component
public class RegionFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String region = request.getHeaders().getFirst("X-Region");
        RegionContextHolder.setRegion(region != null ? region : "US");
        // Process request/response as usual
        try {
            return chain.filter(exchange)
                .doOnTerminate(() -> {
                    // Pseudonymize response status or body metadata if needed
                    System.out.println("Response Status: " + response.getStatusCode());
                });
        } finally {
            RegionContextHolder.clear();
        }
    }
}
