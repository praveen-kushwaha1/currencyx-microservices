package com.currencyx.conversion.dto;

import java.math.BigDecimal;

/**
 * DTO representing the response received from the
 * Currency Exchange Service.
 */
public class CurrencyExchangeResponse {

    private Long id;
    private String from;
    private String to;
    private BigDecimal conversionMultiple;
    private String env;

    public CurrencyExchangeResponse() {
    }

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
}