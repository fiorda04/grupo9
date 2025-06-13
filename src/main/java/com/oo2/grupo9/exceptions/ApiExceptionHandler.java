package com.oo2.grupo9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UsuarioYaExistenteResException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UsuarioYaExistenteResException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(RolyLocalidadException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(RolyLocalidadException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}