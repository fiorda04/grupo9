package com.oo2.grupo9.exceptions;

public class TicketCerradoException extends RuntimeException {
    public TicketCerradoException(String mensaje) {
        super(mensaje);
    }
}