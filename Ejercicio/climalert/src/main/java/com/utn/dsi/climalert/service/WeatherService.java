package com.utn.dsi.climalert.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.utn.dsi.climalert.config.WeatherProperties;
import com.utn.dsi.climalert.dto.WeatherApiResponse;
import com.utn.dsi.climalert.model.RegistroClima;
import com.utn.dsi.climalert.repository.RegistroClimaRepository;

/**
 * Se integra con WeatherAPI para obtener el clima actual de la ubicacion
 * configurada y lo persiste como registro historico.
 */
@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final RestClient weatherRestClient;
    private final WeatherProperties properties;
    private final RegistroClimaRepository repository;

    public WeatherService(RestClient weatherRestClient, WeatherProperties properties,
                          RegistroClimaRepository repository) {
        this.weatherRestClient = weatherRestClient;
        this.properties = properties;
        this.repository = repository;
    }

    /**
     * Consulta el endpoint {@code /current.json} y guarda el resultado.
     *
     * @return el registro persistido, o {@code Optional.empty()} si la consulta fallo.
     */
    public Optional<RegistroClima> consultarYGuardar() {
        try {
            WeatherApiResponse respuesta = weatherRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/current.json")
                            .queryParam("key", properties.apiKey())
                            .queryParam("q", properties.location())
                            .queryParam("aqi", "no")
                            .build())
                    .retrieve()
                    .body(WeatherApiResponse.class);

            if (respuesta == null || respuesta.current() == null) {
                log.warn("WeatherAPI devolvio una respuesta vacia para '{}'", properties.location());
                return Optional.empty();
            }

            RegistroClima registro = mapear(respuesta);
            RegistroClima guardado = repository.save(registro);
            log.info("Clima consultado y guardado -> {}", describir(guardado));
            return Optional.of(guardado);

        } catch (Exception e) {
            log.error("Error al consultar WeatherAPI: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /** Ultima informacion climatica disponible. */
    public Optional<RegistroClima> obtenerUltimo() {
        return repository.findFirstByOrderByConsultadoEnDesc();
    }

    private RegistroClima mapear(WeatherApiResponse r) {
        WeatherApiResponse.Current c = r.current();
        String ubicacion = r.location() != null ? r.location().name() : properties.location();
        String fechaLocal = r.location() != null ? r.location().localtime() : null;
        String condicion = c.condition() != null ? c.condition().text() : "N/D";
        return new RegistroClima(
                ubicacion,
                c.tempC(),
                c.humidity(),
                c.windKph(),
                condicion,
                fechaLocal,
                LocalDateTime.now());
    }

    private String describir(RegistroClima r) {
        return "%s | %.1f C | %d%% humedad | %s".formatted(
                r.getUbicacion(), r.getTemperaturaC(), r.getHumedad(), r.getCondicion());
    }
}
