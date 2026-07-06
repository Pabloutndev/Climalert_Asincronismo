package com.utn.dsi.climalert.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Registro historico de una consulta climatica. Cada 5 minutos se persiste una
 * instancia con los datos actuales devueltos por WeatherAPI.
 */
@Entity
public class RegistroClima {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ubicacion;
    private double temperaturaC;
    private int humedad;
    private double vientoKph;
    private String condicion;

    /** Fecha/hora local informada por el proveedor. */
    private String fechaHoraLocal;

    /** Momento en que este sistema realizo la consulta. */
    private LocalDateTime consultadoEn;

    /** Marca si ya se genero y notifico una alerta para este registro (evita duplicados). */
    private boolean alertaEnviada;

    protected RegistroClima() {
        // Requerido por JPA
    }

    public RegistroClima(String ubicacion, double temperaturaC, int humedad, double vientoKph,
                         String condicion, String fechaHoraLocal, LocalDateTime consultadoEn) {
        this.ubicacion = ubicacion;
        this.temperaturaC = temperaturaC;
        this.humedad = humedad;
        this.vientoKph = vientoKph;
        this.condicion = condicion;
        this.fechaHoraLocal = fechaHoraLocal;
        this.consultadoEn = consultadoEn;
        this.alertaEnviada = false;
    }

    public Long getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public double getTemperaturaC() {
        return temperaturaC;
    }

    public int getHumedad() {
        return humedad;
    }

    public double getVientoKph() {
        return vientoKph;
    }

    public String getCondicion() {
        return condicion;
    }

    public String getFechaHoraLocal() {
        return fechaHoraLocal;
    }

    public LocalDateTime getConsultadoEn() {
        return consultadoEn;
    }

    public boolean isAlertaEnviada() {
        return alertaEnviada;
    }

    public void marcarAlertaEnviada() {
        this.alertaEnviada = true;
    }
}
