package com.utn.dsi.climalert.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.utn.dsi.climalert.service.AlertaService;
import com.utn.dsi.climalert.service.WeatherService;

@Component
public class ClimaScheduler {

    private static final Logger log = LoggerFactory.getLogger(ClimaScheduler.class);

    private final WeatherService weatherService;
    private final AlertaService alertaService;

    public ClimaScheduler(WeatherService weatherService, AlertaService alertaService) {
        this.weatherService = weatherService;
        this.alertaService = alertaService;
    }

    @Scheduled(fixedRateString = "${climalert.scheduling.fetch-rate-ms}")
    public void obtenerClima() {
        log.debug("[Scheduler] Obteniendo clima actual...");
        weatherService.consultarYGuardar();
    }

    @Scheduled(fixedRateString = "${climalert.scheduling.alert-rate-ms}")
    public void procesarAlertas() {
        log.debug("[Scheduler] Analizando ultimo registro climatico...");
        alertaService.analizarUltimo();
    }
}
