package com.oo2.grupo9.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    @ExceptionHandler(TipoNoEncontradoRestException.class)
    public ResponseEntity<Map<String, String>> tipoNoEncontrado(TipoNoEncontradoRestException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(CategoriaNoEncontradaRestException.class)
    public ResponseEntity<Map<String, String>> categoriaNoEncontrada(CategoriaNoEncontradaRestException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
    @ExceptionHandler(TicketNoEncontradoRestException.class)
    public ResponseEntity<Map<String, String>> handleTicketNoEncontradoRestException(TicketNoEncontradoRestException ex) {
    	return ResponseEntity
    			.status(HttpStatus.NOT_FOUND)
    			.body(Map.of("mensaje", ex.getMessage()));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED) 
                .body(Map.of("error", "Credenciales invalidas. Por favor, verifique el usuario y la contraseña."));
    }
    @ExceptionHandler(TicketNoEncontradoConFiltrosRestException.class)
    public ResponseEntity<Map<String, String>> handleTicketNoEncontradoConFiltrosRestException(TicketNoEncontradoConFiltrosRestException ex){
    	return ResponseEntity
    			.status(HttpStatus.NOT_FOUND)
    			.body(Map.of("error", ex.getMessage()));
    }
}