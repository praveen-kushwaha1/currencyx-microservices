
package com.currencyx.conversion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Creates the HTTP client with the Currency Exchange Service base URL.
    @Bean(name = "currencyExchangeHttpClient")
    public RestClient currencyExchangeHttpClient(
            @Value("${currency-exchange.url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
