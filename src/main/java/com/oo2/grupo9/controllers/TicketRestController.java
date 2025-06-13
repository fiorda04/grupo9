package com.oo2.grupo9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo9.services.ITicketService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/tickets")
@Tag(name = "3. Gestion de Tickets", description = "Endpoints para crear, buscar o eliminar tickets")
public class TicketRestController {

	@Autowired
	private ITicketService ticketService;
	
	@PostMapping("/eliminar/{id}")
	@PreAuthorize("hasRole('ROLE_Admin')")
	public ResponseEntity<String> eliminarTicketPorId(@PathVariable Long id){
		ticketService.eliminar(id);
		return ResponseEntity.ok("Ticket eliminado correctamente");
	}
	
}