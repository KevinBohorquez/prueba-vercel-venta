package com.venta.backend.vendedor.application.observer;

public interface IVendedorObservable {

    void addObserver(IVendedorObserver observer);

    void removeObserver(IVendedorObserver observer);

    void notifyObservers(VendedorEvent event);
}
