package com.oo2.grupo9.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequestDTO2(

	@NotBlank(message = "El titulo no puede estar vacio")
	String titulo,
	
	@NotBlank(message = "La descripcion no puede estar vacia")
	String descripcion,
	
	@NotBlank(message = "Debe seleccionar al menos una categoria")
	List<Long> categoriasId,
	
	@NotBlank(message = "La prioridad es obligatoria")
	Long prioridadId,
	
	@NotBlank(message = "La fecha de creacion es obligatoria")
	LocalDate fechaCreacion,
	
	LocalDate fechaCierre,
	
	@NotNull(message = "El estado es obligatorio")
	Long estadoId,
	
	@NotNull(message = "El tipo de ticket es obligatorio")
	Long tipoId,
	
	@NotNull(message = "El ID del cliente es obligatorio")
	Long usuarioIdCliente
) {}