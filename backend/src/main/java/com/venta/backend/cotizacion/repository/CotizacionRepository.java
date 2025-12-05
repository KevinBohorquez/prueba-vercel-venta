package com.venta.backend.cotizacion.repository;

import com.venta.backend.cotizacion.model.Cotizacion;
import com.venta.backend.cotizacion.model.CotizacionEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CotizacionRepository extends JpaRepository<Cotizacion, Integer> {

    Optional<Cotizacion> findByIdAndEstado(Integer id, CotizacionEstado estado);

    Optional<Cotizacion> findByNumCotizacion(String numCotizacion);

    /**
     * Cuenta el número de cotizaciones de un vendedor en estados específicos.
     * El sellerCode se almacena como String en la entidad Cotizacion.
     */
    long countBySellerCodeAndEstadoIn(String sellerCode, List<CotizacionEstado> estados);
}


