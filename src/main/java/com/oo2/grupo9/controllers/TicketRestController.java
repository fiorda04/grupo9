package com.oo2.grupo9.controllers;

import java.util.Map;


import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;


import com.oo2.grupo9.dtos.CrearTicketRequest;
import com.oo2.grupo9.dtos.CrearTicketResponse;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.services.ICategoriaService;
import com.oo2.grupo9.services.IEstadoService;
import com.oo2.grupo9.services.IPrioridadService;
import com.oo2.grupo9.services.ITicketService;
import com.oo2.grupo9.services.ITipoService;
import com.oo2.grupo9.services.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "3-A. Gestión de Tickets", description = "Endpoints para crear, buscar o eliminar tickets")
public class TicketRestController {

	@Autowired
	private ITicketService ticketService;



    public TicketRestController(ITicketService ticketService){
        this.ticketService = ticketService;
    }
	
	@Operation(
	        summary = "Elimina un Ticket mediante el ID (Admin)",
	        description = "Elimina un ticket en el sistema. Requiere rol de Administrador.",
	        security = @SecurityRequirement(name = "Bearer Authentication") 
	    )
	
	@PreAuthorize("hasRole('ROLE_Admin')")
	@PostMapping("/eliminar/{id}")
	public ResponseEntity<Map<String, String>> eliminarTicketPorId(@PathVariable Long id){
		ticketService.eliminar(id);
		return ResponseEntity.ok(Map.of("mensaje", "Ticket eliminado correctamente"));
	}
	
	//Franco Romay
	@Operation(
	        summary = "Crear un nuevo ticket (Cliente)",
	        description = "Crea un nuevo ticket en el sistema. Requiere rol de Cliente.",
	        security = @SecurityRequirement(name = "Bearer Authentication") 
	)
	@PreAuthorize("hasRole('ROLE_Cliente')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CrearTicketResponse> crearTicket(@Valid @RequestBody CrearTicketRequest request) throws Exception {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		CrearTicketResponse nuevoTicket = ticketService.crearTicketDesdeRequest(request, username);
		return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTicket);
	}
	
	//Franco Romay
	@Operation(
	        summary = "Ver un ticket desde un id (Cliente)",
	        description = "Ve el detalle de un ticket en el sistema. Requiere rol de Cliente.",
	        security = @SecurityRequirement(name = "Bearer Authentication") 
	)
	@PreAuthorize("hasRole('ROLE_Cliente')")
	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CrearTicketResponse> verTicket(@PathVariable Long id) throws Exception {
		CrearTicketResponse ticketEncontrado = ticketService.traerResponse(id);
		return ResponseEntity.ok(ticketEncontrado);
	}
	
}