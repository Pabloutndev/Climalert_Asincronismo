package com.utn.dsi.climalert.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utn.dsi.climalert.config.AlertaProperties;
import com.utn.dsi.climalert.model.RegistroClima;
import com.utn.dsi.climalert.repository.RegistroClimaRepository;

/**
 * Analiza la ultima informacion climatica y, si se cumplen las condiciones
 * criticas (temperatura y humedad por encima de los umbrales), dispara la alerta.
 */
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

    /**
     * Evalua el ultimo registro disponible. Si es critico y aun no fue notificado,
     * genera la alerta y delega el envio del correo (asincronico).
     */
    @Transactional
    public void analizarUltimo() {
        Optional<RegistroClima> ultimoOpt = repository.findFirstByOrderByConsultadoEnDesc();

        if (ultimoOpt.isEmpty()) {
            log.debug("Aun no hay datos climaticos para analizar.");
            return;
        }

        RegistroClima ultimo = ultimoOpt.get();

        if (ultimo.isAlertaEnviada()) {
            log.debug("El ultimo registro (id={}) ya fue notificado. Sin novedades.", ultimo.getId());
            return;
        }

        if (esCritico(ultimo)) {
            log.warn("Condicion CRITICA detectada -> {} C / {}%",
                    ultimo.getTemperaturaC(), ultimo.getHumedad());
            emailService.enviarAlerta(ultimo, propiedades.destinatarios());
            ultimo.marcarAlertaEnviada();
            repository.save(ultimo);
        } else {
            log.info("Clima dentro de parametros normales ({} C / {}%). Sin alerta.",
                    ultimo.getTemperaturaC(), ultimo.getHumedad());
        }
    }

    /** Una condicion es critica si temperatura y humedad superan ambos umbrales. */
    private boolean esCritico(RegistroClima r) {
        return r.getTemperaturaC() > propiedades.umbralTemperatura()
                && r.getHumedad() > propiedades.umbralHumedad();
    }
}
