package com.venta.backend.vendedor.application.observer.impl;

import com.venta.backend.vendedor.application.observer.IVendedorObserver;
import com.venta.backend.vendedor.application.observer.VendedorEvent;

public class AuditoriaObserver implements IVendedorObserver {
    @Override
    public void notify(VendedorEvent event) {
        switch (event.getTipoEvento()) {
            case CREADO:
                log.info("Vendedor CREADO - {} (ID: {}, Tipo: {}, Fecha: {})",
                        event.getVendedorNombre(),
                        event.getVendedorId(),
                        event.getSellerType(),
                        event.getTimestamp());
                break;

            case CAMBIO_SEDE:
                String sedeAnterior = (String) event.getDetalle("sedeAnteriorNombre");
                String sedeNueva = (String) event.getDetalle("sedeNuevaNombre");
                log.info("{} cambió de sede: {} → {} (Fecha: {})",
                        event.getVendedorNombre(),
                        sedeAnterior,
                        sedeNueva,
                        event.getTimestamp());
                break;

            case MODIFICADO:
                log.info("Vendedor MODIFICADO - {} (ID: {}, Fecha: {})",
                        event.getVendedorNombre(),
                        event.getVendedorId(),
                        event.getTimestamp());
                break;

            case DESACTIVADO:
                log.info("Vendedor DESACTIVADO - {} (ID: {}, Fecha: {})",
                        event.getVendedorNombre(),
                        event.getVendedorId(),
                        event.getTimestamp());
                break;

            case REACTIVADO:
                log.info("Vendedor REACTIVADO - {} (ID: {}, Fecha: {})",
                        event.getVendedorNombre(),
                        event.getVendedorId(),
                        event.getTimestamp());
                break;
        }
        // auditoriaRepository.save(new AuditoriaVendedor(event));
    }
}


