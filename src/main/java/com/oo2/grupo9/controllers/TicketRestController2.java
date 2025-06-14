package com.oo2.grupo9.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo9.dtos.TicketResponseDTO;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.exceptions.CampoBusquedaVacioException;
import com.oo2.grupo9.exceptions.TicketNoEncontradoConFiltrosRestException;
import com.oo2.grupo9.services.ITicketService;
import com.oo2.grupo9.services.IUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/tickets")
@Tag(name = "4. Gestion de Tickets B", description = "Endpoints para buscar o eliminar tickets")
public class TicketRestController2 {

	@Autowired
	private ITicketService ticketService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Operation(
	        summary = "Elimina un Ticket mediante el ID (Admin)",
	        description = "Elimina un ticket en el sistema. Requiere rol de Administrador.",
	        security = @SecurityRequirement(name = "Bearer Authentication") 
	    )
	
	@PreAuthorize("hasRole('ROLE_Admin')")
	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<Map<String, String>> eliminarTicketPorId(@PathVariable Long id){
		ticketService.eliminar(id);
		return ResponseEntity.ok(Map.of("mensaje", "Ticket eliminado correctamente"));
	}
	
	@Operation(
		    summary = "Buscar tickets del cliente autenticado (filtros opcionales)",
		    description = "Busca tickets filtrados por distintos parámetros, del cliente autenticado.",
		    security = @SecurityRequirement(name = "Bearer Authentication")
		)
		@PreAuthorize("hasRole('ROLE_Cliente')")
		@GetMapping("/buscar-ticket-cliente")
		public ResponseEntity<List<TicketResponseDTO>> buscarTicketsConFiltros(
		    @RequestParam(required = false) String titulo,
		    @RequestParam(required = false) Long categoriaId,
		    @RequestParam(required = false) Long prioridadId,
		    @RequestParam(required = false) Long estadoId,
		    @RequestParam(required = false) Long tipoId,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCreacionDesde,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCreacionHasta,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCierreDesde,
		    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCierreHasta,
		    Principal principal
		) {
		    if ((titulo == null || titulo.isBlank()) &&
		        categoriaId == null && prioridadId == null &&
		        estadoId == null && tipoId == null &&
		        fechaCreacionDesde == null && fechaCreacionHasta == null &&
		        fechaCierreDesde == null && fechaCierreHasta == null) {
		        throw new CampoBusquedaVacioException("Por favor, ingrese al menos un campo para realizar la búsqueda");
		    }

		    Usuario cliente = usuarioService.traer(principal.getName());

		    List<Ticket> tickets = ticketService.buscarTicketsConFiltros(
		        titulo, categoriaId, prioridadId, estadoId, tipoId,
		        fechaCreacionDesde, fechaCreacionHasta,
		        fechaCierreDesde, fechaCierreHasta,
		        cliente
		    );
		    
		    if (tickets.isEmpty()) {
		    	throw new TicketNoEncontradoConFiltrosRestException("No se encontraron tickets con los criterios de búsqueda proporcionados");
		    }

		    List<TicketResponseDTO> ticketDTOs = tickets.stream().map(ticket -> {
                return new TicketResponseDTO(
                    ticket.getIdTicket(),                     
                    ticket.getTitulo(),                    
                    ticket.getDescripcion(),                 
                    ticket.getFechaCreacion() != null ? ticket.getFechaCreacion().toLocalDate() : null, 
                    ticket.getFechaCierre() != null ? ticket.getFechaCierre().toLocalDate() : null,  
                    ticket.getEstado() != null ? ticket.getEstado().getNombreEstado() : null,        
                    ticket.getTipo() != null ? ticket.getTipo().getNombreTipo() : null,               
                    ticket.getPrioridad() != null ? ticket.getPrioridad().getNombrePrioridad() : null, 
                    ticket.getLstCategorias() != null ? ticket.getLstCategorias().stream().map(c -> c.getNombreCategoria()).toList() : null,
                    ticket.getUsuarioCliente() != null ? ticket.getUsuarioCliente().getNombreUsuario() : null 
                );
            }).collect(Collectors.toList());

            return ResponseEntity.ok(ticketDTOs);
        }
}
