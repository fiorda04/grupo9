package com.oo2.grupo9.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo9.services.ITicketService;


@RestController
@RequestMapping("/api/tickets")
public class TicketRestController {

	@Autowired
	private ITicketService ticketService;
	
	@PreAuthorize("hasRole('ROLE_Admin')")
	public ResponseEntity<String> eliminarTicket(@PathVariable Long id){
		ticketService.eliminar(id);
		return ResponseEntity.ok("Ticket eliminado correctamente");
	}
	
}