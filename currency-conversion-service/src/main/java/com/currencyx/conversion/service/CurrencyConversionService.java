package com.currencyx.conversion.service;

import com.currencyx.conversion.dto.CurrencyConversionResponse;
import com.currencyx.conversion.dto.CurrencyExchangeResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

/**
 * Handles currency conversion by retrieving the exchange rate
 * from the Currency Exchange Service.
 */
@Service
public class CurrencyConversionService {

    private final RestTemplate restTemplate;
    private final String exchangeServiceUrl;

    /**
     * Constructor-based dependency injection.
     */
    public CurrencyConversionService(
            RestTemplate restTemplate,
            @Value("${currency-exchange.url}") String exchangeServiceUrl) {

        this.restTemplate = restTemplate;
        this.exchangeServiceUrl = exchangeServiceUrl;
    }

    /**
     * Retrieves the exchange rate and calculates the converted amount.
     */
    public CurrencyConversionResponse convertCurrency(
            String from,
            String to,
            BigDecimal quantity) {

        // Build the URL of the Currency Exchange Service.
        String url = exchangeServiceUrl
                + "/currency-exchange/from/{from}/to/{to}";

        // Call the exchange service and deserialize its JSON response.
        CurrencyExchangeResponse exchangeResponse =
                restTemplate.getForObject(
                        url,
                        CurrencyExchangeResponse.class,
                        from,
                        to
                );

        // Protect against an unexpected empty response.
        if (exchangeResponse == null
                || exchangeResponse.getConversionMultiple() == null) {

            throw new IllegalStateException(
                    "Currency Exchange Service returned no exchange rate."
            );
        }

        // Calculate the converted amount using BigDecimal.
        BigDecimal convertedAmount = quantity.multiply(
                exchangeResponse.getConversionMultiple()
        );

        // Return the complete conversion result.
        return new CurrencyConversionResponse(
                exchangeResponse.getId(),
                exchangeResponse.getFrom(),
                exchangeResponse.getTo(),
                quantity,
                exchangeResponse.getConversionMultiple(),
                convertedAmount,
                exchangeResponse.getEnv()
        );
    }
}