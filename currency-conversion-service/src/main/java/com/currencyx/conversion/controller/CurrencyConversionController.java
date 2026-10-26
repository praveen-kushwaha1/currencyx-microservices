package com.currencyx.conversion.controller;

import com.currencyx.conversion.dto.CurrencyConversionResponse;
import com.currencyx.conversion.service.CurrencyConversionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * Exposes REST endpoints for currency conversion.
 */
@RestController
@RequestMapping("/api/conversion")
public class CurrencyConversionController {

    private final CurrencyConversionService conversionService;

    /**
     * Constructor-based dependency injection.
     */
    public CurrencyConversionController(
            CurrencyConversionService conversionService) {

        this.conversionService = conversionService;
    }

    /**
     * Converts an amount from one currency to another.
     */
    @GetMapping("/from/{from}/to/{to}/amount/{amount}")
    public CurrencyConversionResponse convertCurrency(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal amount) {

        return conversionService.convertCurrency(from, to, amount);
    }
}