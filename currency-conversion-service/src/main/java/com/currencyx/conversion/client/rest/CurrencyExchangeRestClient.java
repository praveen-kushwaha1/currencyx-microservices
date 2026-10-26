
package com.currencyx.conversion.client.rest;

import com.currencyx.conversion.dto.CurrencyExchangeResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CurrencyExchangeRestClient {

    private final RestClient restClient;

    // Injects the configured HTTP client.
    public CurrencyExchangeRestClient(
            @Qualifier("currencyExchangeHttpClient") RestClient restClient) {
        this.restClient = restClient;
    }

    // Calls the Currency Exchange Service to fetch the exchange rate.
    public CurrencyExchangeResponse getExchangeRate(String from, String to) {
        return restClient.get()
                .uri("/currency-exchange/from/{from}/to/{to}", from, to)
                .retrieve()
                .body(CurrencyExchangeResponse.class);
    }
}
