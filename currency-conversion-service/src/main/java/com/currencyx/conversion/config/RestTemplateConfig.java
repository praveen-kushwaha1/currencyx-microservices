package com.currencyx.conversion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configures RestTemplate for synchronous HTTP communication
 * with other microservices.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Creates a RestTemplate bean that can be injected into
     * the service layer.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}