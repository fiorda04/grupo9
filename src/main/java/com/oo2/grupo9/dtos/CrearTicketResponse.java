package com.oo2.grupo9.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record CrearTicketResponse (
	    Long ticketId,
	    String titulo,
	    String descripcion,
	    List<String> nombreCategorias,
	    Long tipoId,
	    String estadoNombre,
	    String prioridadNombre,
	    LocalDateTime fechaCreacion
	) {}
