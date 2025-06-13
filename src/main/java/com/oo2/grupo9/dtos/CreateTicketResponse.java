package com.oo2.grupo9.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class CreateTicketResponse {

	private Long id;
	private String titulo;
	private String descripcion;
	private String tipo;
	private String estado;
	private String prioridad;
	private List<Long> categoriaId;
	private LocalDateTime fechaCreacion;
	
	
}
