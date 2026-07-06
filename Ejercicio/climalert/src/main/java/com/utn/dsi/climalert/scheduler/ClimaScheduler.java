package com.utn.dsi.climalert.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.utn.dsi.climalert.service.AlertaService;
import com.utn.dsi.climalert.service.WeatherService;

/**
 * Nucleo del asincronismo del sistema: define las dos tareas programadas que
 * mantienen a Climalert funcionando de forma autonoma.
 *
 * <ul>
 *   <li>Cada 5 minutos: obtiene y guarda el clima actual.</li>
 *   <li>Cada 1 minuto: analiza el ultimo dato y dispara alertas.</li>
 * </ul>
 *
 * Los intervalos se leen de {@code application.properties} para poder acelerarlos
 * durante las pruebas.
 */
@Component
public class ClimaScheduler {

    private static final Logger log = LoggerFactory.getLogger(ClimaScheduler.class);

    private final WeatherService weatherService;
    private final AlertaService alertaService;

    public ClimaScheduler(WeatherService weatherService, AlertaService alertaService) {
        this.weatherService = weatherService;
        this.alertaService = alertaService;
    }

    /** Punto 1 del enunciado: obtencion periodica del clima (cada 5 min por defecto). */
    @Scheduled(fixedRateString = "${climalert.scheduling.fetch-rate-ms}")
    public void obtenerClima() {
        log.debug("[Scheduler] Obteniendo clima actual...");
        weatherService.consultarYGuardar();
    }

    /** Punto 2 del enunciado: procesamiento de alertas (cada 1 min por defecto). */
    @Scheduled(fixedRateString = "${climalert.scheduling.alert-rate-ms}")
    public void procesarAlertas() {
        log.debug("[Scheduler] Analizando ultimo registro climatico...");
        alertaService.analizarUltimo();
    }
}
