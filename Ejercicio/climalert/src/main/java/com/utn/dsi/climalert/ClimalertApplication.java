package com.utn.dsi.climalert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Punto de entrada de Climalert.
 *
 * <p>El servicio no tiene interfaz grafica: su ciclo de vida esta gobernado por
 * tareas programadas ({@code @EnableScheduling}) y el envio de correos se realiza
 * de forma asincronica ({@code @EnableAsync}), que es el foco de este TP.</p>
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@ConfigurationPropertiesScan
public class ClimalertApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClimalertApplication.class, args);
    }
}
