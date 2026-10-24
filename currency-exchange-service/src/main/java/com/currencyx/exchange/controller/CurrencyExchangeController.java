package com.currencyx.exchange.controller;

import com.currencyx.exchange.model.CurrencyExchange;
import com.currencyx.exchange.repository.CurrencyExchangeReposistory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
public class CurrencyExchangeController {

	private final CurrencyExchangeReposistory repository;
	private final Environment environment;

	/**
	 * Constructor-based Dependency Injection.
	 * Spring automatically injects the repository and environment beans.
	 *
	 * @param repository the data access repository for currency exchange records
	 * @param environment Spring's Environment abstraction for fetching runtime properties
	 */
	public CurrencyExchangeController(CurrencyExchangeReposistory repository, Environment environment) {
		this.repository = repository;
		this.environment = environment;
	}

	@GetMapping(value = "/welcome")
	public String welcome() {
		return "Welcome to Currency Exchange Service";
	}

	@GetMapping(value = "/currency-exchange", produces = {"application/json"})
	public CurrencyExchange getExchange() {
		return new CurrencyExchange(1L, "USD", "INR", BigDecimal.TEN, "Unknown");
	}

	/**
	 * Retrieves currency exchange data dynamically from the H2 database based on source and target currencies.
	 * Also stamps the response with the active local server port (useful for load balancing and multi-instance demos).
	 *
	 * @param from the source currency code (e.g., USD)
	 * @param to the target currency code (e.g., INR)
	 * @return the matching CurrencyExchange entity with populated server port environment info
	 * @throws ResponseStatusException with HTTP 404 status if the pair is not found in the database
	 */
	@GetMapping(value = "/currency-exchange/from/{from}/to/{to}", produces = {"application/json"})
	public CurrencyExchange getCurrencyExchange(@PathVariable String from, @PathVariable String to) {

		// Fetch exchange record from the repository layer
		CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);

		if (currencyExchange == null) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND,
					"Currency exchange rate not found for from: " + from + " to: " + to
			);
		}

		// Dynamically grab the running server port (e.g., 8000) from the environment
		String serverPort = environment.getProperty("local.server.port");
		currencyExchange.setEnv(serverPort);

		return currencyExchange;
	}
}