package com.oo2.grupo9.controllers;


import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/tickets")
@Tag(name = "3. Gestion de Tickets", description = "Endpoints para crear, buscar o eliminar tickets")
public class TicketRestController {

	@Autowired
	private ITicketService ticketService;
	@Autowired
	private IUsuarioService usuarioService;

	
	private ModelMapper modelMapper = new ModelMapper();

    public TicketRestController(ITicketService ticketService, IUsuarioService usuarioService){
        this.ticketService = ticketService;
        this.usuarioService = usuarioService;
    }
	
	@PostMapping("/eliminar/{id}")
	@PreAuthorize("hasRole('ROLE_Admin')")
	public ResponseEntity<String> eliminarTicketPorId(@PathVariable Long id){
		ticketService.eliminar(id);
		return ResponseEntity.ok("Ticket eliminado correctamente");
	}
	
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
	
}