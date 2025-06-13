package com.oo2.grupo9.dtos;
import java.time.LocalDateTime;

public record TraerIntervencionResponse(
	Long id,
    String contenido,
    LocalDateTime fecha,
    String autorNombre,
    Long ticketId
) {}