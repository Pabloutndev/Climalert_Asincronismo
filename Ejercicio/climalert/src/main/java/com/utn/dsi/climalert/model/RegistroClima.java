package com.utn.dsi.climalert.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    private String fechaHoraLocal;
    private LocalDateTime consultadoEn;

    protected RegistroClima() {
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

    public boolean esCritico(double umbralTemperatura, int umbralHumedad) {
        return this.temperaturaC > umbralTemperatura && this.humedad > umbralHumedad;
    }
}
