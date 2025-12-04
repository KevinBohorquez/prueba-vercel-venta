package com.venta.backend.vendedor.entities;

import com.venta.backend.vendedor.enums.SellerStatus;
import com.venta.backend.vendedor.enums.SellerType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Vendedor")
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vendedor")
    private Long idVendedor;

    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "primer_nombre", nullable = false)
    private String primerNombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "numero_telefono", length = 15)
    private String numeroTelefono;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDate fechaRegistro;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vendedor", nullable = false)
    private SellerType tipoVendedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_vendedor", nullable = false)
    private SellerStatus estadoVendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sede", nullable = false)
    private Sede sede;

    /**
     * Comprueba si el vendedor está activo.
     * @return true si el estado es ACTIVE, false de lo contrario.
     */
    public boolean isActive() {
        return this.estadoVendedor == SellerStatus.ACTIVE;
    }

    /**
     * Cambia el estado del vendedor (para baja lógica o reactivación).
     * @param nuevoEstado El nuevo estado (ACTIVE o INACTIVE).
     */
    public void cambiarEstado(SellerStatus nuevoEstado) {
        this.estadoVendedor = nuevoEstado;
    }

    /**
     * Asigna o reasigna al vendedor a una nueva sede.
     * @param nuevaSede La nueva entidad Sede.
     */
    public void asignarSede(Sede nuevaSede) {
        this.sede = nuevaSede;
    }

    /**
     * Devuelve el nombre completo del vendedor.
     * @return String con "primerNombre apellido".
     */
    public String getNombreCompleto() {
        return this.primerNombre + " " + this.apellido;
    }
}