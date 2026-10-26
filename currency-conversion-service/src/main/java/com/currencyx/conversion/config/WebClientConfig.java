
package com.currencyx.conversion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    // Creates the HTTP client configured with the exchange service URL.
    @Bean(name = "currencyExchangeHttpWebClient")
    public WebClient currencyExchangeHttpWebClient(
            @Value("${currency-exchange.url}") String baseUrl) {

        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
