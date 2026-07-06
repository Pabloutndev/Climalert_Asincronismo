package com.utn.dsi.climalert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "climalert.weather")
public record WeatherProperties(
        String baseUrl,
        String apiKey,
        String location
) {
}
