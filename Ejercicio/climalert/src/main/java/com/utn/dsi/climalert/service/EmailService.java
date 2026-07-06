package com.utn.dsi.climalert.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.utn.dsi.climalert.model.RegistroClima;

/**
 * Envia por correo el detalle del clima cuando se genera una alerta.
 *
 * <p>El metodo {@link #enviarAlerta} es {@code @Async}: se ejecuta en un hilo
 * separado para no bloquear la tarea programada que lo dispara. Este es el
 * ejemplo central de asincronismo del TP.</p>
 *
 * <p>Con {@code climalert.mail.enabled=false} el correo no se envia por SMTP
 * sino que se registra en el log, para poder probar el flujo sin un servidor
 * de correo real.</p>
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${climalert.mail.enabled:false}")
    private boolean mailHabilitado;

    @Value("${climalert.mail.remitente:no-reply@climalert.com}")
    private String remitente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void enviarAlerta(RegistroClima registro, List<String> destinatarios) {
        String asunto = "[ALERTA CLIMATICA] " + registro.getUbicacion();
        String cuerpo = construirCuerpo(registro);

        if (!mailHabilitado) {
            log.warn("""
                    [SIMULACION DE CORREO - mail deshabilitado]
                    Para: {}
                    Asunto: {}
                    {}""", destinatarios, asunto, cuerpo);
            return;
        }

        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setFrom(remitente);
            mensaje.setTo(destinatarios.toArray(new String[0]));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);
            mailSender.send(mensaje);
            log.info("Correo de alerta enviado a {}", destinatarios);
        } catch (Exception e) {
            log.error("No se pudo enviar el correo de alerta: {}", e.getMessage());
        }
    }

    private String construirCuerpo(RegistroClima r) {
        return """
                Se ha detectado una condicion climatica critica.

                Ubicacion:        %s
                Temperatura:      %.1f C
                Humedad:          %d %%
                Viento:           %.1f km/h
                Condicion:        %s
                Hora local:       %s
                Consultado (sist):%s

                -- Climalert (sistema automatico de monitoreo)""".formatted(
                r.getUbicacion(),
                r.getTemperaturaC(),
                r.getHumedad(),
                r.getVientoKph(),
                r.getCondicion(),
                r.getFechaHoraLocal(),
                r.getConsultadoEn());
    }
}
