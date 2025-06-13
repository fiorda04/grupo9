package com.oo2.grupo9.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record CrearTicketResponse (

	Long id,
	String titulo,
	String descripcion,
	String tipo,
	String estado,
	String prioridad,
	List<Long> categoriaId,
	LocalDateTime fechaCreacion
	
	
) {}