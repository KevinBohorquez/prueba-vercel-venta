package com.venta.backend.vendedor.entities;

import com.venta.backend.vendedor.enums.SellerStatus;
import com.venta.backend.vendedor.enums.SellerType;
import com.venta.backend.vendedor.enums.DocumentType;

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


    @Column(name = "employee_rrhh_id")
    private Long employeeRrhhId; // ID del trabajador en el módulo de RRHH

    // Para vendedores EXTERNOS que tienen RUC (persona jurídica)
    @Column(length = 11, unique = true)
    private String ruc; // Solo para vendedores externos con factura

    // Información bancaria para pagos de comisiones
    @Column(length = 20)
    private String bankAccount; // Cuenta bancaria

    @Column(length = 50)
    private String bankName; // Nombre del banco

    // Tipo de documento de identidad (por si hay extranjeros)
    @Enumerated(EnumType.STRING)
    @Column(name = "id_document_type")
    private DocumentType documentType; // DNI, CE, PASAPORTE

    /*
    * significa que la información de la sede (Sede)
    * solo se carga de la base de datos cuando realmente
    * se necesita (acceso diferido), optimizando el rendimiento.
    * */
    @ManyToOne(fetch = FetchType.LAZY) // Relación: Muchos Vendedores van a una Sede
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