package com.venta.backend.vendedor.infraestructura.clientes.imp;

import com.venta.backend.vendedor.infraestructura.clientes.IClienteRRHH;
import com.venta.backend.vendedor.infraestructura.clientes.dto.EmpleadoRRHHDTO;
import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Implementación SIMULADA del cliente RRHH
 * En producción, esto haría llamadas HTTP reales al módulo RRHH
 */
@Component
public class ClienteRRHHImpl implements IClienteRRHH {

    // private final RestTemplate restTemplate;
    // private final String rrhhBaseUrl = "http://rrhh-service-ip/api"; // URL de configuración

    @Override
    public Optional<EmpleadoRRHHDTO> getEmployeeByDni(String dni) {

        /*
        try {
            // URL de ejemplo para la búsqueda por DNI
            String url = rrhhBaseUrl + "/empleados/dni/" + dni;

            // Suponemos que el endpoint devuelve un array de 1 elemento, o solo el objeto si se encuentra.
            // EmpleadoRRHHDTO[] response = restTemplate.getForObject(url, EmpleadoRRHHDTO[].class);

            // Si devuelve solo el objeto:
            // EmpleadoRRHHDTO response = restTemplate.getForObject(url, EmpleadoRRHHDTO.class);
            // return Optional.ofNullable(response);

        } catch (HttpClientErrorException.NotFound ex) {
            // Manejar 404 (No encontrado)
            return Optional.empty();
        } catch (Exception ex) {
            // Manejar errores de conexión u otros errores 5xx
            return Optional.empty();
        }
        */

        // Datos de prueba simulados
        if (dni.equals("12345678")) {
            EmpleadoRRHHDTO empleado = new EmpleadoRRHHDTO();

            // 🚨 USANDO LOS NUEVOS NOMBRES DE CAMPOS
            empleado.setIdEmpleado(1001L); // Clave foránea numérica en vendedor
            empleado.setDocumentoIdentidad("12345678");
            empleado.setNombres("Juan");
            empleado.setApellidoPaterno("Pérez");
            empleado.setApellidoMaterno("García");
            empleado.setEmail("juan.perez@empresa.com");
            empleado.setTelefono("987654321");
            empleado.setDireccion("Av. Los Olivos 123, Lima");

            return Optional.of(empleado);
        }

        if (dni.equals("87654321")) {
            EmpleadoRRHHDTO empleado = new EmpleadoRRHHDTO();

            // 🚨 SEGUNDO EJEMPLO
            empleado.setIdEmpleado(1002L); // Clave foránea numérica en vendedor
            empleado.setDocumentoIdentidad("87654321");
            empleado.setNombres("María");
            empleado.setApellidoPaterno("López");
            empleado.setApellidoMaterno("Sánchez");
            empleado.setEmail("maria.lopez@empresa.com");
            empleado.setTelefono("987654322");
            empleado.setDireccion("Av. La Marina 456, Lima");

            return Optional.of(empleado);
        }

        // Si no encuentra el DNI, retorna Optional vacío
        return Optional.empty();
    }
}
