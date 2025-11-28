package com.venta.backend.vendedor.application.observer.impl;

import com.venta.backend.vendedor.application.observer.EventType;
import com.venta.backend.vendedor.application.observer.IVendedorObserver;
import com.venta.backend.vendedor.application.observer.VendedorEvent;
import com.venta.backend.vendedor.enums.SellerType;

public class NotificadorFinanzasObserver implements IVendedorObserver {
    @Override
    public void notify(VendedorEvent event) {
        if (event.getTipoEvento() == EventType.CREADO) {
            if (event.getSellerType() == SellerType.EXTERNAL) {
                log.info("[OBSERVER - Finanzas] Cuenta de COMISIONES creada para vendedor externo {} (ID: {})",
                        event.getVendedorNombre(),
                        event.getVendedorId());
            } else {
                log.info("[OBSERVER - Finanzas] Cuenta de BONOS POR METAS creada para vendedor interno {} (ID: {})",
                        event.getVendedorNombre(),
                        event.getVendedorId());
            }

            // finanzasService.crearCuentaComisiones(event.getVendedorId(), event.getSellerType());
        }
    }
}
