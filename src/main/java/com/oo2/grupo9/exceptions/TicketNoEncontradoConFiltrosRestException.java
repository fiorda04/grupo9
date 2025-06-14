package com.oo2.grupo9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketNoEncontradoConFiltrosRestException extends RuntimeException {

	public TicketNoEncontradoConFiltrosRestException(String message) {
		super(message);
	}
	
	public TicketNoEncontradoConFiltrosRestException(String message, Throwable cause) {
		super(message, cause);
	}
	
}