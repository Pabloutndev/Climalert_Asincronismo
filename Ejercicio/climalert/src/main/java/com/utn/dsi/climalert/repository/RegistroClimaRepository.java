package com.utn.dsi.climalert.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utn.dsi.climalert.model.RegistroClima;

public interface RegistroClimaRepository extends JpaRepository<RegistroClima, Long> {

    /** Devuelve el ultimo registro climatico consultado (el mas reciente). */
    Optional<RegistroClima> findFirstByOrderByConsultadoEnDesc();
}
