package com.oo2.grupo9.exceptions;

public class TicketNoEncontradoRestException extends RuntimeException{
	
	public TicketNoEncontradoRestException(String mensaje) {
		super(mensaje);
	}

}