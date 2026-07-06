package com.utn.dsi.climalert.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuracion de la integracion con el proveedor externo WeatherAPI.
 * Se mapea desde las propiedades {@code climalert.weather.*}.
 */
@ConfigurationProperties(prefix = "climalert.weather")
public record WeatherProperties(
        String baseUrl,
        String apiKey,
        String location
) {
}
