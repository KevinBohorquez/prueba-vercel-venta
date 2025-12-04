package com.venta.backend.vendedor.application.impl;

import com.venta.backend.vendedor.application.dto.request.RegistroVendedorRequest;
import com.venta.backend.vendedor.application.estrategias.IRegistroVendedorStrategia;
import com.venta.backend.vendedor.application.exceptions.RecursoNoEncontradoException;
import com.venta.backend.vendedor.application.exceptions.RegistroVendedorException;
import com.venta.backend.vendedor.entities.Vendedor;
import com.venta.backend.vendedor.enums.SellerStatus;
import com.venta.backend.vendedor.infraestructura.clientes.IClienteRRHH;
import com.venta.backend.vendedor.infraestructura.clientes.dto.EmpleadoRRHHDTO;
import com.venta.backend.vendedor.infraestructura.repository.VendedorRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor // Para agregar las dependencias
public class RegistroInternoStrategiaImpl implements IRegistroVendedorStrategia {

    private final VendedorRepositorio vendedorRepositorio;
    private final IClienteRRHH clienteRRHH;

    @Override
    public void validateData(RegistroVendedorRequest request) {
        // Empleado ya está registrado como Vendedor"
        if (vendedorRepositorio.existsByDni(request.getDni())) {
            throw new RegistroVendedorException("El DNI ya se encuentra registrado como vendedor.");
        }
    }

    @Override
    public Vendedor createSellerEntity(RegistroVendedorRequest request) {

        String dniToSearch = request.getDni();
        EmpleadoRRHHDTO empleadoRRHH = clienteRRHH.getEmployeeByDni(dniToSearch)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Empleado no encontrado en RRHH. Verifique el DNI."
                ));

        return Vendedor.builder()
                .dni(empleadoRRHH.getDni())
                .primerNombre(empleadoRRHH.getFirstName())
                .apellido(empleadoRRHH.getLastName())
                .email(empleadoRRHH.getEmail())
                .numeroTelefono(empleadoRRHH.getPhoneNumber())
                .direccion(empleadoRRHH.getAddress())
                .tipoVendedor(request.getSellerType()) // Será INTERNAL
                .estadoVendedor(SellerStatus.ACTIVE) // Estado por defecto
                .fechaRegistro(LocalDate.now())
                // La Sede se asignará en el Servicio principal
                .build();
    }
}
