package com.oo2.grupo9.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.ITicketService;
import com.oo2.grupo9.services.implementation.RolService;
import com.oo2.grupo9.services.implementation.UsuarioService;

@Controller
@RequestMapping("/")
public class HomeController {

    private final RolService rolService;

    private UsuarioService usuarioService;
    private ITicketService ticketService;

    public HomeController(UsuarioService usuarioService, ITicketService ticketService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.ticketService = ticketService;
        this.rolService = rolService;
    }

    @GetMapping
    public String mostrarPaginaInicio(Model model,
                                      @RequestParam(required = false) String mensaje,
                                      @RequestParam(required = false) String error,
                                      @RequestParam(required = false) String success,
                                      @RequestParam(value = "keyword", required = false) String keyword) {

        // 1. Manejo de mensajes de URL
        if (mensaje != null) {
            model.addAttribute("mensaje", mensaje);
        }
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("mensaje", success);
        }

        // 2. Obtener la autenticación actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si el usuario está autenticado y no es anónimo
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            // Lógica para el rol 'Cliente'
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Cliente"))) {
                String username = auth.getName();
                try {
                    Usuario cliente = usuarioService.traer(username);
                    
                    if (cliente != null) { 
                        List<Ticket> tickets = ticketService.traerPorCliente(cliente.getIdUsuario());
                        
                        if (keyword != null && !keyword.trim().isEmpty()) {
                            String lowerCaseKeyword = keyword.trim().toLowerCase();
                            tickets = tickets.stream()
                                             .filter(t -> t.getTitulo() != null && t.getTitulo().toLowerCase().contains(lowerCaseKeyword))
                                             .collect(Collectors.toList());
                            model.addAttribute("keyword", keyword); // Mantener la palabra clave en el input
                        }
                        
                        
                        model.addAttribute("tickets", tickets);

                        // --- CAMBIOS AQUÍ para acceder al nombreEstado del DTO ---
                        long totalTickets = tickets.size();
                        long ticketsAbiertos = tickets.stream()
                                                    .filter(t -> t.getEstado() != null && !"Cerrado".equalsIgnoreCase(t.getEstado().getNombreEstado()))
                                                    .count();
                        long ticketsResueltos = tickets.stream()
                                                    .filter(t -> t.getEstado() != null && "Cerrado".equalsIgnoreCase(t.getEstado().getNombreEstado())) // Asumiendo que "Cerrado" es el estado de resuelto
                                                    .count();
                        // --------------------------------------------------------

                        model.addAttribute("totalTickets", totalTickets);
                        model.addAttribute("ticketsAbiertos", ticketsAbiertos);
                        model.addAttribute("ticketsResueltos", ticketsResueltos);
                    }
                } catch (IllegalArgumentException e) {
                    model.addAttribute("error", "No se pudo cargar la información del usuario: " + e.getMessage());
                }
                return ViewRouteHelper.INDEX;

            // Lógica para el rol 'Admin'
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {
                model.addAttribute("usuarios", usuarioService.traerTodos()); 
                model.addAttribute("todosLosRoles", rolService.traerTodos());
                return ViewRouteHelper.INDEX;
                
            // Lógica para el rol 'Empleado'
            } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("ROLE_Empleado"))) {
                String username = auth.getName();
                try {
                	Usuario empleado = usuarioService.traer(username);
                	if (empleado != null) {
                	    List<Ticket> ticketsAsignados = ticketService.traerTodos(); //.traerTodos(); no anda
                	    if (keyword != null && !keyword.trim().isEmpty()) {
                            String lowerCaseKeyword = keyword.trim().toLowerCase();
                            ticketsAsignados = ticketsAsignados.stream()
                                             .filter(t -> t.getTitulo() != null && t.getTitulo().toLowerCase().contains(lowerCaseKeyword))
                                             .collect(Collectors.toList());
                            model.addAttribute("keyword", keyword); // Mantener la palabra clave en el input
                        }

                	    model.addAttribute("tickets", ticketsAsignados);
                	}

                } catch (Exception e) {
                    model.addAttribute("error", "No se pudo obtener los tickets del empleado: " + e.getMessage());
                }
                return ViewRouteHelper.INDEX;
                
            } else {
                return ViewRouteHelper.INDEX;
            }
        } else {
            // Usuario no autenticado (anónimo)
            return ViewRouteHelper.INDEX; 
        }
    }

    @PostMapping("/dar-de-baja/{id}")
    public String darDeBajaUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("ID RECIBIDO EN DAR DE BAJA: " + id);
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario dado de baja exitosamente.");
            return "redirect:/";
        } catch (Exception e) {
            System.err.println("¡ERROR AL DAR DE BAJA USUARIO!");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al dar de baja al usuario: " + e.getMessage());
            return "redirect:/";
        }
    }

    @PostMapping("/dar-de-alta/{id}") 
    public String darDeAltaUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        System.out.println("ID RECIBIDO EN DAR DE ALTA (HomeController): " + id);
        try {
            usuarioService.darDeAlta(id); 
            redirectAttributes.addFlashAttribute("mensaje", "Usuario dado de alta exitosamente.");
            return "redirect:/"; 
        } catch (Exception e) {
            System.err.println("¡ERROR AL DAR DE ALTA USUARIO!");
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error al dar de alta al usuario: " + e.getMessage());
            return "redirect:/";
        }
    }
    
    @GetMapping("/tickets/IntervencionesEmpleado")
    public String verTicketsConMisIntervenciones(Model model, Authentication auth) {
        String username = auth.getName();
        try {
            Usuario empleado = usuarioService.traer(username);
            List<Intervencion> intervenciones = ticketService.traerIntervencionesPorEmpleado(empleado.getIdUsuario());
            model.addAttribute("intervenciones", intervenciones);
        } catch (Exception e) {
            model.addAttribute("error", "No se pudieron obtener tus intervenciones: " + e.getMessage());
        }
        return "tickets/IntervencionesEmpleado";
    }
    
    
    
}