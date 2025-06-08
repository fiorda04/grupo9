package com.oo2.grupo9.exceptions;

public class UsuarioYaExistenteException extends RuntimeException {
    public UsuarioYaExistenteException(String message) {
        super(message);
    }
}
