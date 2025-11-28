package com.venta.backend.vendedor.application.observer;

public interface IVendedorObserver {
    void notify(VendedorEvent event);
}
