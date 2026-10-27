
package com.currencyx.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * Logs incoming requests passing through the API Gateway.
 *
 * A GlobalFilter applies to all Gateway routes.
 */
@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            GatewayFilterChain chain) {

        // Read request details before forwarding to the backend.
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getURI().getPath();

        log.info("Incoming request: method={}, path={}", method, path);

        // Continue processing through the configured Gateway route.
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Run this filter early in the filter chain.
        return -1;
    }
}
