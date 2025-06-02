package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.*;
import com.oo2.grupo9.dtos.IntervencionDTO;
import com.oo2.grupo9.dtos.TicketCreacionDTO; 
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class TicketController {

    @Autowired
    private ICategoriaService categoriaService;

    @Autowired
    private ITipoService tipoService;

    @Autowired
    private IEstadoService estadoService;

    @Autowired
    private IPrioridadService prioridadService;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ITicketService ticketService;



    @GetMapping("/tickets/crear")
    public ModelAndView mostrarFormularioCrearTicket() {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKET_CREAR);
        mAV.addObject("nuevoTicketDTO", new TicketCreacionDTO()); // Pasa el nuevo DTO vacío
        mAV.addObject("categoriasDisponibles", categoriaService.traerTodas());
        mAV.addObject("tiposDisponibles", tipoService.traerTodas());
        return mAV;
    }

    @PostMapping("/tickets/guardar")
    public ModelAndView guardarNuevoTicket(@ModelAttribute("nuevoTicketDTO") @Valid TicketCreacionDTO nuevoTicketDTO,
                                             BindingResult bindingResult) {

        ModelAndView mAV = new ModelAndView();

        if (bindingResult.hasErrors()) {
            mAV.setViewName(ViewRouteHelper.TICKET_CREAR);
            mAV.addObject("categoriasDisponibles", categoriaService.traerTodas());
            mAV.addObject("tiposDisponibles", tipoService.traerTodas());
            return mAV;
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Usuario cliente = usuarioService.traer(username); // Asumo que este método trae el Usuario por nombre de usuario

            if (cliente == null) {
                mAV.setView(new RedirectView(ViewRouteHelper.ROUTE_INDEX + "?error=No se pudo encontrar el usuario autenticado.", true));
                return mAV;
            }

            // Mapeo manual en lugar de ModelMapper para depurar
            Ticket nuevoTicket = new Ticket();
            nuevoTicket.setTitulo(nuevoTicketDTO.getTitulo());
            nuevoTicket.setDescripcion(nuevoTicketDTO.getDescripcion());

            // Las fechas de creación y cierre, y el estado inicial, se configuran
            // en el constructor de la entidad Ticket o se asignan aca.
            // Si la entidad Ticket tiene @CreationTimestamp en fechaCreacion, no necesitas esto.
            // Si la entidad Ticket tiene @UpdateTimestamp en fechaCierre, no necesitas esto.
            nuevoTicket.setFechaCreacion(LocalDate.now()); // Asegurate de que no estas duplicando @CreationTimestamp
            nuevoTicket.setFechaCierre(null); // Asegúrate de que no estás duplicando @UpdateTimestamp

            Tipo tipo = tipoService.traer(nuevoTicketDTO.getTipoId());
            Prioridad prioridad = prioridadService.traer(1L); // Prioridad predeterminada: 1 (ej. "Baja")
            Estado estadoAbierto = estadoService.traer(1L);   // Estado "Abierto": 1

            List<Categoria> categoriasSeleccionadas = nuevoTicketDTO.getCategoriasId().stream()
                    .map(categoriaService::traer)
                    .collect(Collectors.toList());

            nuevoTicket.setUsuarioCliente(cliente);
            nuevoTicket.setLstCategorias(categoriasSeleccionadas);
            nuevoTicket.setTipo(tipo);
            nuevoTicket.setPrioridad(prioridad);
            nuevoTicket.setEstado(estadoAbierto);

            ticketService.guardar(nuevoTicket);

            mAV.setView(new RedirectView(ViewRouteHelper.ROUTE_INDEX + "?mensaje=Ticket creado exitosamente!", true));
            return mAV;

        } catch (Exception e) {
            e.printStackTrace(); // Imprime la traza completa para depurar
            mAV.setViewName(ViewRouteHelper.TICKET_CREAR);
            mAV.addObject("nuevoTicketDTO", nuevoTicketDTO);
            mAV.addObject("categoriasDisponibles", categoriaService.traerTodas());
            mAV.addObject("tiposDisponibles", tipoService.traerTodas());
            mAV.addObject("error", "Error al crear el ticket: " + e.getMessage());
            return mAV;
        }
    }
    
    @GetMapping("tickets/VerTicket/{id}")
	public ModelAndView verTicket(@PathVariable("id") Long id) {
		ModelAndView mAV = new ModelAndView(ViewRouteHelper.VER_TICKET);
	    Ticket ticket = ticketService.traer(id);
	    mAV.addObject("ticket", ticket);
	    
//	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//	    boolean esEmpleado = auth != null && auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_Empleado"));
//
//	    if (esEmpleado) {
//	        mAV.addObject("intervencionDTO", new IntervencionDTO());
//	        mAV.addObject("estadosDisponibles", estadoService.traerTodas());
//	        mAV.addObject("prioridadesDisponibles", prioridadService.traerTodas());
//	    } else {
//	        // Para que Thymeleaf no rompa, paso valores vacíos
//	        mAV.addObject("intervencionDTO", null);
//	        mAV.addObject("estadosDisponibles", Collections.emptyList());
//	        mAV.addObject("prioridadesDisponibles", Collections.emptyList());
//	    }
	    return mAV;
	}
    
    
//    @PreAuthorize("hasRole('ROLE_Empleado')")
//    @GetMapping("tickets/modificar/{id}" Long id)
//    public ModelAndView mostrarFormularioModificarTicket() {
//        
//       
//    }
//    
//    @PreAuthorize("hasRole('ROLE_Empleado')")
//    @PostMapping("tickets/nuevaModificacion")
//    public ModelAndView guardarNuevaModificacion() {
//        
//        
//    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("tickets/modificar/{id}")
    public String eliminarTicket(@PathVariable("id") Long id) {
      ticketService.eliminar(id);
      return "redirect:" + ViewRouteHelper.ROUTE_INDEX;    
    }
    
}