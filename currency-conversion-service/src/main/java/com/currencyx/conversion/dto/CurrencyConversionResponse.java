package com.currencyx.conversion.dto;

import java.math.BigDecimal;

/**
 * DTO representing the final currency conversion result.
 */
public class CurrencyConversionResponse {

    private Long id;
    private String from;
    private String to;
    private BigDecimal quantity;
    private BigDecimal conversionMultiple;
    private BigDecimal totalCalculatedAmount;
    private String env;

    public CurrencyConversionResponse() {
    }

    public CurrencyConversionResponse(
            Long id,
            String from,
            String to,
            BigDecimal quantity,
            BigDecimal conversionMultiple,
            BigDecimal totalCalculatedAmount,
            String env) {

        this.id = id;
        this.from = from;
        this.to = to;
        this.quantity = quantity;
        this.conversionMultiple = conversionMultiple;
        this.totalCalculatedAmount = totalCalculatedAmount;
        this.env = env;
    }

    public Long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getConversionMultiple() {
        return conversionMultiple;
    }

    public BigDecimal getTotalCalculatedAmount() {
        return totalCalculatedAmount;
    }

    public String getEnv() {
        return env;
    }
}