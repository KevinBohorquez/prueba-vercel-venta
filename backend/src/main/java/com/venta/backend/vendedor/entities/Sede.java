package com.venta.backend.vendedor.entities;

import com.venta.backend.vendedor.enums.BranchType;
import jakarta.persistence.*;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Sede")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Long idSede;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sede", nullable = false)
    private BranchType tipoSede;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    @Column(name = "status")
    private boolean active;

    @Column(name = "warehouse_ref_id")
    private Long warehouseRefId; // ID del almacén en el módulo de Inventario
}