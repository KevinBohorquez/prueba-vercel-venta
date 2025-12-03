package com.venta.backend.vendedor.application.controller;

import com.venta.backend.vendedor.application.exceptions.RecursoNoEncontradoException;
import com.venta.backend.vendedor.application.exceptions.RegistroVendedorException;
import com.venta.backend.venta.application.exceptions.ItemProductoNoEncontradoException;
import com.venta.backend.venta.application.exceptions.VentaNoEncontradaException;
import com.venta.backend.venta.application.exceptions.VentaOperacionNoPermitidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones
 * Convierte excepciones en respuestas HTTP apropiadas
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            RecursoNoEncontradoException.class,
            VentaNoEncontradaException.class,
            ItemProductoNoEncontradoException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler({
            RegistroVendedorException.class,
            VentaOperacionNoPermitidaException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Map<String, Object>> handleBadRequest(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "Ocurrió un error inesperado: " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
