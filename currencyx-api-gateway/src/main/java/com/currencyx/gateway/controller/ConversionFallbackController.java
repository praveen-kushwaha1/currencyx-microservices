package com.currencyx.gateway.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * Provides a controlled response when the conversion route fails
 * or its circuit breaker is open.
 */
@RestController
public class ConversionFallbackController {

    /**
     * Gateway forwards failed conversion requests to this endpoint.
     *
     * HTTP 503 tells the client that the service is temporarily unavailable.
     * We must not return a fabricated exchange rate.
     */
    @RequestMapping("/fallback/conversion")
    public Mono<ResponseEntity<Map<String, String>>> conversionFallback() {

        Map<String, String> response = Map.of(
                "status", "SERVICE_UNAVAILABLE",
                "message", "Currency conversion is temporarily unavailable. Please try again later."
        );

        return Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(response)
        );
    }
}