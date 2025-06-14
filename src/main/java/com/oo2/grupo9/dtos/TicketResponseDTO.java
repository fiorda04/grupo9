package com.oo2.grupo9.dtos;

import java.time.LocalDate;
import java.util.List;

public record TicketResponseDTO(

		Long id,
		String titulo,
		String descripcion,
		LocalDate fechaCreacion,
		LocalDate fechaCierre,
		String estadoNombre,
		String tipoNombre,
		String prioridadNombre,
		List<String> categoriasNombre,
		String nombreUsuarioCliente
) {}
