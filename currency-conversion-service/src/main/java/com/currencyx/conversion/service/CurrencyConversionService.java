package com.currencyx.conversion.service;

import com.currencyx.conversion.client.feign.CurrencyExchangeFeignClient;
import com.currencyx.conversion.client.rest.CurrencyExchangeRestClient;
import com.currencyx.conversion.client.webclient.CurrencyExchangeWebClient;
import com.currencyx.conversion.dto.CurrencyConversionResponse;
import com.currencyx.conversion.dto.CurrencyExchangeResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Handles currency conversion using four different HTTP clients:
 *
 * 1. RestTemplate
 * 2. OpenFeign
 * 3. RestClient
 * 4. WebClient
 *
 * Exchange rate retrieval differs between clients, but the
 * conversion calculation is shared.
 */
@Service
public class CurrencyConversionService {

    private final RestTemplate restTemplate;
    private final CurrencyExchangeFeignClient feignClient;
    private final CurrencyExchangeRestClient restClient;
    private final CurrencyExchangeWebClient webClient;
    private final String exchangeServiceUrl;

    /**
     * Constructor-based dependency injection.
     *
     * Spring injects all four HTTP client dependencies.
     */
    public CurrencyConversionService(
            RestTemplate restTemplate,
            CurrencyExchangeFeignClient feignClient,
            CurrencyExchangeRestClient restClient,
            CurrencyExchangeWebClient webClient,
            @Value("${currency-exchange.url}") String exchangeServiceUrl) {

        this.restTemplate = restTemplate;
        this.feignClient = feignClient;
        this.restClient = restClient;
        this.webClient = webClient;
        this.exchangeServiceUrl = exchangeServiceUrl;
    }

    /**
     * Converts currency using RestTemplate.
     *
     * RestTemplate performs a synchronous HTTP GET request.
     */
    public CurrencyConversionResponse convertUsingRestTemplate(
            String from,
            String to,
            BigDecimal quantity) {

        validateRequest(from, to, quantity);

        // Build the Currency Exchange Service endpoint.
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

        // Calculate the converted amount using the shared method.
        return buildConversionResponse(exchangeResponse, quantity);
    }

    /**
     * Converts currency using OpenFeign.
     *
     * Feign generates the HTTP client implementation automatically.
     */
    public CurrencyConversionResponse convertUsingFeign(
            String from,
            String to,
            BigDecimal quantity) {

        validateRequest(from, to, quantity);

        // Retrieve the exchange rate through the Feign interface.
        CurrencyExchangeResponse exchangeResponse =
                feignClient.getExchangeRate(from, to);

        return buildConversionResponse(exchangeResponse, quantity);
    }

    /**
     * Converts currency using RestClient.
     *
     * RestClient provides a modern synchronous HTTP API.
     */
    public CurrencyConversionResponse convertUsingRestClient(
            String from,
            String to,
            BigDecimal quantity) {

        validateRequest(from, to, quantity);

        // Retrieve the exchange rate using RestClient.
        CurrencyExchangeResponse exchangeResponse =
                restClient.getExchangeRate(from, to);

        return buildConversionResponse(exchangeResponse, quantity);
    }

    /**
     * Converts currency using WebClient.
     *
     * Mono allows the HTTP response to be processed reactively.
     */
    public Mono<CurrencyConversionResponse> convertUsingWebClient(
            String from,
            String to,
            BigDecimal quantity) {

        validateRequest(from, to, quantity);

        // Retrieve and transform the response without blocking.
        return webClient.getExchangeRate(from, to)
                .map(exchangeResponse ->
                        buildConversionResponse(
                                exchangeResponse,
                                quantity
                        )
                );
    }

    /**
     * Performs the shared currency conversion calculation.
     *
     * All four HTTP client implementations use this method,
     * avoiding duplicated business logic.
     */
    private CurrencyConversionResponse buildConversionResponse(
            CurrencyExchangeResponse exchangeResponse,
            BigDecimal quantity) {

        // Validate the exchange service response.
        if (exchangeResponse == null
                || exchangeResponse.getConversionMultiple() == null) {

            throw new IllegalStateException(
                    "Currency Exchange Service returned no exchange rate."
            );
        }

        // Extract and validate the exchange rate.
        BigDecimal rate = exchangeResponse.getConversionMultiple();

        if (rate.signum() <= 0) {
            throw new IllegalStateException(
                    "Currency Exchange Service returned an invalid exchange rate."
            );
        }

        // Calculate the converted amount.
        BigDecimal convertedAmount = quantity.multiply(rate);

        // Build the final conversion response.
        return new CurrencyConversionResponse(
                exchangeResponse.getId(),
                exchangeResponse.getFrom(),
                exchangeResponse.getTo(),
                quantity,
                rate,
                convertedAmount,
                exchangeResponse.getEnv()
        );
    }
    private static final Logger log =
            LoggerFactory.getLogger(CurrencyConversionService.class);

    public void testLogging() {

        log.info("Currency conversion request received");

        log.warn("Currency exchange rate is unavailable");

        log.error("Currency conversion failed");
    }

    /**
     * Validates the currency codes and requested amount.
     */
    private void validateRequest(
            String from,
            String to,
            BigDecimal quantity) {

        if (from == null || !from.matches("[A-Za-z]{3}")) {
            throw new IllegalArgumentException(
                    "Source currency must be a three-letter currency code."
            );
        }

        if (to == null || !to.matches("[A-Za-z]{3}")) {
            throw new IllegalArgumentException(
                    "Target currency must be a three-letter currency code."
            );
        }

        if (quantity == null || quantity.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }
    }
}