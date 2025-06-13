package com.oo2.grupo9.dtos;

public record CrearTicketRequest(

	String titulo,
	String descripcion,
	Long idCategoria,
	Long idTipo,
	Long idPrioridad,
	Long idUsuario
	
	
) {}