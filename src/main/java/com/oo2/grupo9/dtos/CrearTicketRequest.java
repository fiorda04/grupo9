package com.oo2.grupo9.dtos;

import java.util.List;

public class CrearTicketRequest {

	private String titulo;
	private String descripcion;
	private Long tipoId;
	private Long estadoId;
	private Long prioridadId;
	private List<Long> categoriaId;
	
	
}
