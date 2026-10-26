package com.currencyx.conversion.client.feign;

import com.currencyx.conversion.dto.CurrencyExchangeResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Declarative HTTP client for the Currency Exchange Service.
 *
 * Spring Cloud OpenFeign generates the implementation automatically.
 */
@FeignClient(
        name = "currency-exchange-client",
        url = "${currency-exchange.url}"
)
public interface CurrencyExchangeFeignClient {

    /**
     * Retrieves the exchange rate for the specified currency pair.
     *
     * @param from source currency code
     * @param to   target currency code
     * @return exchange rate details
     */
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyExchangeResponse getExchangeRate(
            @PathVariable("from") String from,
            @PathVariable("to") String to
    );
}