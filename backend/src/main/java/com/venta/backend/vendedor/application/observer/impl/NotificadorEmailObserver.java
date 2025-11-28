package com.venta.backend.vendedor.application.observer.impl;

import com.venta.backend.vendedor.application.observer.IVendedorObserver;
import com.venta.backend.vendedor.application.observer.VendedorEvent;

public class NotificadorEmailObserver implements IVendedorObserver {
    @Override
    public void notify(VendedorEvent event) {
        switch (event.getTipoEvento()) {
            case CREADO:
                log.info("Enviando email de BIENVENIDA a {}",
                        event.getVendedorNombre());
                break;

            case CAMBIO_SEDE:
                String sedeNueva = (String) event.getDetalle("sedeNuevaNombre");
                log.info("Notificando a {} sobre cambio a sede: {}",
                        event.getVendedorNombre(),
                        sedeNueva);
                break;

            case DESACTIVADO:
                log.info("Enviando email de DESPEDIDA a {}",
                        event.getVendedorNombre());
                break;
        }

        // emailService.enviarEmail(event);
    }
}



