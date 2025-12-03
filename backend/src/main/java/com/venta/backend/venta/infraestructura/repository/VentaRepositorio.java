package com.venta.backend.venta.infraestructura.repository;

import com.venta.backend.venta.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepositorio extends JpaRepository<Venta, Long> {
}

