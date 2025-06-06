package com.oo2.grupo9.dtos;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class TicketDTO {
	
		@NotEmpty(message = "El titulo del ticket no puede estar vacio")
		@Size(min = 6, max = 30,  message = "El titulo no puede ser menor a 6 ni mayor a 30")
	  	private String titulo;
		
		@NotEmpty(message = "La descripcion del ticket no puede estar vacio")
	    private String descripcion;
		
//		@NotEmpty(message = "Debe seleccionar al menos una categoría.")
//	    private List<String> nombresCategorias;
		
		private List<Long> categoriasId;

//	    @NotNull(message = "La prioridad del ticket no puede estar vacia")
//	    private String nombrePrioridades;
	    
	    @NotNull(message = "La prioridad del ticket no puede estar vacia")
        private Long prioridadId;
	    
	    @NotNull(message = "La fecha de creacion del ticket no puede estar vacia")
	    private LocalDate fechaCreacion;
	    
	    @Future(message = "La fecha de cierre del ticket se va a dar cuando el ticket se cierre")
	    private LocalDate fechaCierre;
	    
//	    @NotNull(message = "El estado del ticket no puede estar vacio")
//	    private String nombreEstado;
	    
	    @NotNull(message = "El estado del ticket no puede estar vacio")
        private Long estadoId;
	    
	    @NotNull(message = "El tipo del ticket no puede estar vacio")
        private Long tipoId;
	    
//	    @NotNull(message = "El tipo del ticket no puede estar vacio")
//	    private String nombreTipo;   
	    
//	    @NotNull(message = "El usuario del ticket no puede estar vacio")
//	    private String usuarioNombreCliente; 
	    
	    @NotNull(message = "El usuario del ticket no puede estar vacio")
        private Long usuarioIdCliente; 
	    
//	    private List<Intervencion> lstIntervenciones; 
	    
	    private List<Long> intervencionesId;

}
