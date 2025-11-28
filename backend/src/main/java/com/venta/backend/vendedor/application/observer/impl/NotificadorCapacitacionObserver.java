package com.venta.backend.vendedor.application.observer.impl;

import com.venta.backend.vendedor.application.observer.EventType;
import com.venta.backend.vendedor.application.observer.IVendedorObserver;
import com.venta.backend.vendedor.application.observer.VendedorEvent;

public class NotificadorCapacitacionObserver implements IVendedorObserver {
    @Override
    public void notify(VendedorEvent event) {
        // Solo reaccionar a creaciones
        if (event.getTipoEvento() == EventType.CREADO) {
            log.info("Vendedor {} (ID: {}) será inscrito en curso de inducción",
                    event.getVendedorNombre(),
                    event.getVendedorId());

            // capacitacionService.inscribirEnCursoInduccion(event.getVendedorId());
        }
    }
}



