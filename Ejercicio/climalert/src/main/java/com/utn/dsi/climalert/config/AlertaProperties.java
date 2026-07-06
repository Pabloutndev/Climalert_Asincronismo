package com.utn.dsi.climalert.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "climalert.alerta")
public record AlertaProperties(
        double umbralTemperatura,
        int umbralHumedad,
        List<String> destinatarios
) {
}
