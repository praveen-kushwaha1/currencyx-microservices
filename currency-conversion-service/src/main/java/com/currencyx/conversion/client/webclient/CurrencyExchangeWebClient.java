
package com.currencyx.conversion.client.webclient;

import com.currencyx.conversion.dto.CurrencyExchangeResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CurrencyExchangeWebClient {

    private final WebClient webClient;

    // Injects the configured WebClient bean using its unique name.
    public CurrencyExchangeWebClient(
            @Qualifier("currencyExchangeHttpWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    // Calls the Currency Exchange Service asynchronously.
    public Mono<CurrencyExchangeResponse> getExchangeRate(String from, String to) {
        return webClient.get()
                .uri("/currency-exchange/from/{from}/to/{to}", from, to)
                .retrieve()
                .bodyToMono(CurrencyExchangeResponse.class);
    }
}
