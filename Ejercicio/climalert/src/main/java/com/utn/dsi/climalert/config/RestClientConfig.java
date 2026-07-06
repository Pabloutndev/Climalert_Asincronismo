package com.utn.dsi.climalert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Provee el {@link RestClient} usado para consultar WeatherAPI, configurado con
 * la URL base tomada de {@link WeatherProperties}.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient weatherRestClient(WeatherProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
