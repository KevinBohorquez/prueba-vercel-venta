package com.venta.backend.venta.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GuardarProductosRequest {
    
    @NotNull(message = "La lista de productos es requerida")
    private List<ProductoCarrito> productos;
    
    @Data
    public static class ProductoCarrito {
        @NotNull(message = "El ID del producto es requerido")
        private Long idProducto;
        
        @NotNull(message = "El nombre del producto es requerido")
        private String nombreProducto;
        
        @NotNull(message = "El precio unitario es requerido")
        private BigDecimal precioUnitario;
        
        @NotNull(message = "La cantidad es requerida")
        @Min(value = 1, message = "La cantidad mínima es 1")
        private Integer cantidad;
    }
}
