package com.oo2.grupo9.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oo2.grupo9.dtos.CrearIntervencionRequest;
import com.oo2.grupo9.dtos.CrearIntervencionResponse;
import com.oo2.grupo9.dtos.IntervencionDTO;
import com.oo2.grupo9.dtos.TraerIntervencionResponse;
import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.services.implementation.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/intervenciones")
@Tag(name = "5. Gestion de Intervenciones", description = "Endpoints para administrar intervenciones.")
public class IntervencionRestController {

    
    @Autowired
    private TicketService ticketService;
    
    
    @Operation(
        summary = "Agregar una intervención",
        description = "Agrega una nueva intervención a un ticket. Solo empleados pueden intervenir.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasRole('ROLE_Empleado')")
    @PostMapping
    public ResponseEntity<CrearIntervencionResponse> agregarIntervencion(
            @Valid @RequestBody CrearIntervencionRequest request
    ) {
        Intervencion nueva = ticketService.realizarIntervencionDesdeRequest(request);

        CrearIntervencionResponse response = new CrearIntervencionResponse(
                nueva.getTicket().getIdTicket(),
                nueva.getAutor().getNombreUsuario(),
                nueva.getContenido(),
                nueva.getFechaIntervencion()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
    	    summary = "Obtener intervenciones por ID de empleado",
    	    description = "Devuelve las intervenciones realizadas por un empleado dado su ID.",
    	    security = @SecurityRequirement(name = "Bearer Authentication")
    	)
    	@PreAuthorize("hasAnyRole('ROLE_Admin', 'ROLE_Empleado')")
    	@GetMapping("/{id}")
    	public ResponseEntity<List<TraerIntervencionResponse>> obtenerPorId(@PathVariable Long id) {
    	    List<Intervencion> intervenciones = ticketService.traerIntervencionesPorEmpleado(id);

    	    List<TraerIntervencionResponse> response = intervenciones.stream().map(i -> new TraerIntervencionResponse(
    	            i.getIdIntervencion(),
    	            i.getContenido(),
    	            i.getFechaIntervencion(),
    	            i.getAutor().getNombreUsuario(),
    	            i.getTicket().getIdTicket()
    	        ))
    	        .toList();

    	    return ResponseEntity.ok(response);
    	}
}