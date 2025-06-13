package com.oo2.grupo9.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo9.services.ITicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/tickets")
@Tag(name = "3. Gestión de Tickets", description = "Endpoints para crear, buscar o eliminar tickets")
public class TicketRestController {

	@Autowired
	private ITicketService ticketService;
	
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
	
}