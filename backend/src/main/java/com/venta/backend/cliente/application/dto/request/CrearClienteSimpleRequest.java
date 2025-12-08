package com.venta.backend.cliente.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
// Clientes clientes y mas clientes
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearClienteSimpleRequest {

    @Pattern(regexp = "\\d{8}", message = "DNI debe tener exactamente 8 dígitos")
    private String dni;
    
    @NotNull(message = "Nombre es obligatorio")
    @NotBlank(message = "Nombre no puede estar vacío")
    private String firstName;
    
    @NotNull(message = "Apellido es obligatorio")
    @NotBlank(message = "Apellido no puede estar vacío")
    private String lastName;
    
    @Email(message = "Email debe ser válido")
    private String email;
    
    @NotNull(message = "Teléfono es obligatorio")
    @NotBlank(message = "Teléfono no puede estar vacío")
    private String phoneNumber;
    
    private String telefonoFijo;
    private LocalDate fechaNacimiento;
}

