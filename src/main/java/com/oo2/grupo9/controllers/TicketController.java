package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.*;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;


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
    public String mostrarFormularioCrearTicket(Model model) {
        model.addAttribute("nuevoTicket", new Ticket()); // Para el binding del formulario
        model.addAttribute("categoriasDisponibles", categoriaService.traerTodas());
        model.addAttribute("tiposDisponibles", tipoService.traerTodas());
        return ViewRouteHelper.TICKET_CREAR;
    }

    @PostMapping("/tickets/guardar")
    public String guardarNuevoTicket(@ModelAttribute("nuevoTicket") Ticket nuevoTicket,
                                     @RequestParam(value = "categorias", required = false) List<Long> categoriaIds,
                                     @RequestParam("tipo") Long tipoId,
                                     Model model,
                                     HttpServletRequest request) {

        if (categoriaIds == null || categoriaIds.isEmpty()) {
            model.addAttribute("errorCategorias", "Debe seleccionar al menos una categoría.");
            model.addAttribute("categoriasDisponibles", categoriaService.traerTodas());
            model.addAttribute("tiposDisponibles", tipoService.traerTodas());
            return ViewRouteHelper.TICKET_CREAR; // Volver al formulario con error
        }

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario cliente = usuarioService.traer(username);

        // Obtener las entidades necesarias
        Tipo tipo = tipoService.traer(tipoId);
        Prioridad prioridad = prioridadService.traer(1L); // Prioridad predeterminada: 1
        Estado estadoAbierto = estadoService.traer(1L);   // Estado "Abierto": 1

        // Obtener las categorías seleccionadas
        List<Categoria> categoriasSeleccionadas = categoriaIds.stream()
                .map(categoriaService::traer)
                .collect(Collectors.toList());

        // Setear los atributos del nuevo ticket
        nuevoTicket.setUsuarioCliente(cliente);
        nuevoTicket.setLstCategorias(categoriasSeleccionadas);
        nuevoTicket.setTipo(tipo);
        nuevoTicket.setPrioridad(prioridad);
        nuevoTicket.setEstado(estadoAbierto);
        nuevoTicket.setFechaCreacion(LocalDate.now());
        nuevoTicket.setFechaCierre(null);

        // Guardar el nuevo ticket
        ticketService.guardar(nuevoTicket);

        return ViewRouteHelper.TICKET_CREAR; // Redirigir a la lista de tickets del cliente
    }
}