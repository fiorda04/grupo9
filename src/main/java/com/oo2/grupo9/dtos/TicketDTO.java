package com.oo2.grupo9.dtos;

import java.time.LocalDate;
import java.util.List;

import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Intervencion;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TicketDTO {
	
		@NotEmpty(message = "El titulo del ticket no puede estar vacio")
		@Size(min = 6, max = 30,  message = "El titulo no puede ser menor a 6 ni mayor a 30")
	  	private String titulo;
		
		@NotEmpty(message = "La descripcion del ticket no puede estar vacio")
	    private String descripcion;
		
	    private List<Long> categoriasId;

	    
	    @NotNull(message = "La prioridad del ticket no puede estar vacia")
	    private Long prioridadId;
	    
	    @NotNull(message = "La fecha de creacion del ticket no puede estar vacia")
	    private LocalDate fechaCreacion;
	    
	    @Future(message = "La fecha de cierre del ticket se va a dar cuando el ticket se cierre")
	    private LocalDate fechaCierre;
	    
	    @NotNull(message = "El estado del ticket no puede estar vacio")
	    private Long estadoId;
	    
	    @NotNull(message = "El tipo del ticket no puede estar vacio")
	    private Long tipoId;   
	    
	    @NotNull(message = "El usuario del ticket no puede estar vacio")
	    private Long usuarioIdCliente; 
	    
	    private List<Long> intervencionesId; 

}
