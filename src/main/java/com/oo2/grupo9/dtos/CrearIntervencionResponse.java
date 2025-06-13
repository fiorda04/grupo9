package com.oo2.grupo9.dtos;
import java.time.LocalDateTime;

public record CrearIntervencionResponse(
	Long ticketId,
    String autor,
    String contenido,
    LocalDateTime fecha
) {}