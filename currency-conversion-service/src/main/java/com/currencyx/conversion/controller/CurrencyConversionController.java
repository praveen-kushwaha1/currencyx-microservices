package com.currencyx.conversion.controller;

import com.currencyx.conversion.dto.CurrencyConversionResponse;
import com.currencyx.conversion.service.CurrencyConversionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Exposes currency conversion endpoints using four HTTP clients.
 *
 * The controller handles HTTP requests and delegates the business
 * logic to CurrencyConversionService.
 */
@RestController
@RequestMapping("/api/conversion")
public class CurrencyConversionController {

    private final CurrencyConversionService conversionService;

    /**
     * Injects the conversion service using constructor injection.
     */
    public CurrencyConversionController(
            CurrencyConversionService conversionService) {

        this.conversionService = conversionService;
    }

    /**
     * Converts currency using RestTemplate.
     *
     * Example:
     * GET /api/conversion/resttemplate/from/USD/to/INR/amount/100
     */
    @GetMapping("/resttemplate/from/{from}/to/{to}/amount/{amount}")
    public CurrencyConversionResponse convertUsingRestTemplate(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal amount) {

        return conversionService.convertUsingRestTemplate(
                from,
                to,
                amount
        );
    }

    /**
     * Converts currency using OpenFeign.
     *
     * Example:
     * GET /api/conversion/feign/from/USD/to/INR/amount/100
     */
    @GetMapping("/feign/from/{from}/to/{to}/amount/{amount}")
    public CurrencyConversionResponse convertUsingFeign(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal amount) {

        return conversionService.convertUsingFeign(
                from,
                to,
                amount
        );
    }

    /**
     * Converts currency using RestClient.
     *
     * Example:
     * GET /api/conversion/restclient/from/USD/to/INR/amount/100
     */
    @GetMapping("/restclient/from/{from}/to/{to}/amount/{amount}")
    public CurrencyConversionResponse convertUsingRestClient(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal amount) {

        return conversionService.convertUsingRestClient(
                from,
                to,
                amount
        );
    }

    /**
     * Converts currency using reactive WebClient.
     *
     * Example:
     * GET /api/conversion/webclient/from/USD/to/INR/amount/100
     */
    @GetMapping("/webclient/from/{from}/to/{to}/amount/{amount}")
    public Mono<CurrencyConversionResponse> convertUsingWebClient(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal amount) {

        return conversionService.convertUsingWebClient(
                from,
                to,
                amount
        );
    }
}