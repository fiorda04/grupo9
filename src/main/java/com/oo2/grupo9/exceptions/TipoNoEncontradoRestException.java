package com.oo2.grupo9.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TipoNoEncontradoRestException extends RuntimeException {
    public TipoNoEncontradoRestException(String message) {
        super(message);
    }
}