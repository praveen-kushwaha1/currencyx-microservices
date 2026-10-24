package com.currencyx.exchange.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Table(name="currency")
@Entity
public class CurrencyExchange {
    @Id 
	private Long id;
    @Column(name="CURRENCY_FROM")
	private String from;
    @Column(name="CURRENCY_TO")
	private String to;
    @Column(name="CONVERSION_MULTIPLE")
	private BigDecimal conversionMultiple;
	private String env;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public BigDecimal getConversionMultiple() {
		return conversionMultiple;
	}
	public void setConversionMultiple(BigDecimal conversionMultiple) {
		this.conversionMultiple = conversionMultiple;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	
	
	public CurrencyExchange() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public CurrencyExchange(Long id, String from, String to, BigDecimal conversionMultiple, String env) {
		super();
		this.id = id;
		this.from = from;
		this.to = to;
		this.conversionMultiple = conversionMultiple;
		this.env = env;
	}
}
