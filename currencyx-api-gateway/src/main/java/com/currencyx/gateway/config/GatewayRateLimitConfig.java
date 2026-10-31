
package com.currencyx.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import reactor.core.publisher.Mono;

/**
 * Configures the client identity used by the Gateway rate limiter.
 */
@Configuration
public class GatewayRateLimitConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            // Identify the caller using its network address.
            var address = exchange.getRequest().getRemoteAddress();

            if (address == null || address.getAddress() == null) {
                return Mono.just("unknown");
            }

            return Mono.just(address.getAddress().getHostAddress());
        };
    }
}
