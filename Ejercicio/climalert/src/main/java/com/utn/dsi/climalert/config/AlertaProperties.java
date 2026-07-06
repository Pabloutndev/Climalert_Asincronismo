package com.utn.dsi.climalert.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Umbrales que definen una condicion climatica critica y destinatarios de la alerta.
 * Se mapea desde las propiedades {@code climalert.alerta.*}.
 */
@ConfigurationProperties(prefix = "climalert.alerta")
public record AlertaProperties(
        double umbralTemperatura,
        int umbralHumedad,
        List<String> destinatarios
) {
}
