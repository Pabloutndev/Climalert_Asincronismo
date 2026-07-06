package com.utn.dsi.climalert.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.utn.dsi.climalert.model.RegistroClima;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final String remitente;

    public EmailService(JavaMailSender mailSender,
                        @Value("${climalert.mail.remitente:no-reply@climalert.com}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    @Async
    public void enviarAlerta(RegistroClima registro, List<String> destinatarios) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatarios.toArray(new String[0]));
        mensaje.setSubject("[ALERTA CLIMATICA] " + registro.getUbicacion());
        mensaje.setText(construirCuerpo(registro));

        try {
            mailSender.send(mensaje);
            log.info("Correo de alerta enviado a {}", destinatarios);
        } catch (MailException e) {
            log.error("No se pudo enviar el correo de alerta a {}: {}", destinatarios, e.getMessage());
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
