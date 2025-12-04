package com.venta.backend.vendedor.application.impl;

import com.venta.backend.vendedor.application.dto.request.ModificacionVendedorRequest;
import com.venta.backend.vendedor.application.estrategias.IEdicionVendedorStrategia;
import com.venta.backend.vendedor.entities.Sede;
import com.venta.backend.vendedor.entities.Vendedor;
import org.springframework.stereotype.Service;

@Service
public class EdicionExternaStrategiaImpl implements IEdicionVendedorStrategia {

    @Override
    public void applyChanges(Vendedor sellerToUpdate, ModificacionVendedorRequest request, Sede newBranch) {

        // Si el DTO trae un nuevo valor, lo actualiza. Si no (es null), deja el que ya tenía.
        if (request.getLastName() != null) {
            sellerToUpdate.setApellido(request.getLastName());
        }
        if (request.getEmail() != null) {
            sellerToUpdate.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            sellerToUpdate.setNumeroTelefono(request.getPhoneNumber());
        }
        if (request.getAddress() != null) {
            sellerToUpdate.setDireccion(request.getAddress());
        }

        if (newBranch != null) {
            sellerToUpdate.asignarSede(newBranch);
        }

        if (request.getSellerStatus() != null) {
            sellerToUpdate.cambiarEstado(request.getSellerStatus());
        }
    }
}
