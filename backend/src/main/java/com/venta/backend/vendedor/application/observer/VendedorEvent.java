package com.venta.backend.vendedor.application.observer;

import com.venta.backend.vendedor.enums.SellerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendedorEvent {

    private EventType tipoEvento;
    private Long vendedorId;
    private String vendedorNombre;
    private SellerType sellerType;
    private LocalDateTime timestamp;

    @Builder.Default
    private Map<String, Object> detallesAdicionales = new HashMap<>();

    // Métodos helper para agregar detalles
    public void addDetalle(String clave, Object valor) {
        this.detallesAdicionales.put(clave, valor);
    }

    public Object getDetalle(String clave) {
        return this.detallesAdicionales.get(clave);
    }
}
