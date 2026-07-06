package com.utn.dsi.climalert.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.utn.dsi.climalert.config.AlertaProperties;
import com.utn.dsi.climalert.model.RegistroClima;
import com.utn.dsi.climalert.repository.RegistroClimaRepository;

@Service
public class AlertaService {

    private static final Logger log = LoggerFactory.getLogger(AlertaService.class);

    private final RegistroClimaRepository repository;
    private final AlertaProperties propiedades;
    private final EmailService emailService;

    public AlertaService(RegistroClimaRepository repository, AlertaProperties propiedades,
                         EmailService emailService) {
        this.repository = repository;
        this.propiedades = propiedades;
        this.emailService = emailService;
    }

    public void analizarUltimo() {
        Optional<RegistroClima> ultimoOpt = repository.findFirstByOrderByConsultadoEnDesc();

        if (ultimoOpt.isEmpty()) {
            log.debug("Aun no hay datos climaticos para analizar.");
            return;
        }

        RegistroClima ultimo = ultimoOpt.get();

        if (ultimo.esCritico(propiedades.umbralTemperatura(), propiedades.umbralHumedad())) {
            log.warn("Condicion CRITICA detectada -> {} C / {}%",
                    ultimo.getTemperaturaC(), ultimo.getHumedad());
            emailService.enviarAlerta(ultimo, propiedades.destinatarios());
        } else {
            log.info("Clima dentro de parametros normales ({} C / {}%). Sin alerta.",
                    ultimo.getTemperaturaC(), ultimo.getHumedad());
        }
    }
}
