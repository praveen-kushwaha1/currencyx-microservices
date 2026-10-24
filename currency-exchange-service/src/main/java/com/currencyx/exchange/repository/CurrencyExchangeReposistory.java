package com.currencyx.exchange.repository;


import com.currencyx.exchange.model.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeReposistory extends JpaRepository<CurrencyExchange, Long> {

	CurrencyExchange findByFromAndTo(String from , String to);
}
