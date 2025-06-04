package com.oo2.grupo9.controllers;

import com.oo2.grupo9.entities.*;
import com.oo2.grupo9.dtos.TicketCreacionDTO; 
import com.oo2.grupo9.dtos.TicketDTO; 
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.*;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
public class TicketController {

    private final PasswordEncoder passwordEncoder;

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
    
    private ModelMapper modelMapper = new ModelMapper();


    TicketController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }



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
            Usuario cliente = usuarioService.traer(username); 

            if (cliente == null) {
                mAV.setView(new RedirectView(ViewRouteHelper.ROUTE_INDEX + "?error=No se pudo encontrar el usuario autenticado.", true));
                return mAV;
            }
            Ticket nuevoTicket = new Ticket();
            nuevoTicket.setTitulo(nuevoTicketDTO.getTitulo());
            nuevoTicket.setDescripcion(nuevoTicketDTO.getDescripcion());
            nuevoTicket.setFechaCreacion(LocalDate.now()); 
            nuevoTicket.setFechaCierre(null); 

            Tipo tipo = tipoService.traer(nuevoTicketDTO.getTipoId()).get();
            Prioridad prioridad = prioridadService.traer(1L).get(); // Prioridad predeterminada: 1
            Estado estadoAbierto = estadoService.traer(1L).get();   // Estado "Abierto": 1

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
//	    TicketDTO ticketDTO = modelMapper.map(ticketService.traer(id).get(), TicketDTO.class);
	    Ticket ticket = ticketService.traer(id).get();
	    mAV.addObject("ticket", ticket);
	    return mAV;
	}
    
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("tickets/modificar/{id}")
    public String eliminarTicket(@PathVariable("id") Long id) {
        ticketService.eliminar(id);
        return "redirect:" + ViewRouteHelper.ROUTE_INDEX;    
    }
    
    @GetMapping("/tickets/buscar/tipo")
    public String buscarTicketsPorTipo(@RequestParam("valor") Long idTipo, Model model) {
        List<Ticket> resultados = new ArrayList<>();
        String nombreTipoBuscado;
        
        resultados = ticketService.traerTicketsPorTipo(idTipo);
    	Optional<Tipo> tipoOpt = tipoService.traer(idTipo);
    	if(tipoOpt.isPresent()) {
    		nombreTipoBuscado = tipoOpt.get().getNombreTipo();
    	}else {
    		nombreTipoBuscado = "ID de Tipo " + idTipo + " (desconocido)";
    	}
    	
//        if(idTipo != null) {
//        	(aca va todo el desarrollo de arriba si puede estar vacio)
//        }else {
//        	nombreTipoBuscado = "Ningún tipo seleccionado";
//        }
        
        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Tipo: " + nombreTipoBuscado);
        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/tickets/buscar/cliente")
    public String buscarTicketsPorCliente(@RequestParam("valor") Long idUsuario, Model model) {
        List<Ticket> resultados = ticketService.traerPorCliente(idUsuario);
        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Usuario: " + usuarioService.traer(idUsuario).getNombreUsuario());
        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/tickets/buscar/empleado")
    public String buscarTicketsPorEmpleado(@RequestParam("valor") Long idAutor, Model model) {
        List<Ticket> resultados = ticketService.traerPorEmpleado(idAutor);
        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Usuario: " + usuarioService.traer(idAutor).getNombreUsuario());
        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    @GetMapping("/tickets/buscar/estado")
    public String buscarTicketsPorEstado(@RequestParam("valor") Long idEstado, Model model) {
        List<Ticket> resultados = new ArrayList<>();
        String nombreEstadoBuscado;
        resultados = ticketService.traerTicketPorEstado(idEstado);
    	Optional<Estado> estadoOpt = estadoService.traer(idEstado);
    	
    	if(estadoOpt.isPresent()) {
    		nombreEstadoBuscado = estadoOpt.get().getNombreEstado();
    	}
    	else {
    		nombreEstadoBuscado = "ID de Estado " + idEstado + " (desconocido)";
    	}
    	
//        if(idEstado != null) {
//        	(aca va todo el desarrollo de arriba si puede estar vacio)
//        }else {
//        	nombreEstadoBuscado = "Ningún estado seleccionado";
//        }
    	
        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Estado: " + nombreEstadoBuscado);
        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    @GetMapping("/tickets/buscar/prioridad")
    public String buscarTicketsPorPrioridad(@RequestParam("valor") Long idPrioridad, Model model) {
        List<Ticket> resultados = new ArrayList<>();
        String nombrePrioridadBuscado;
        resultados = ticketService.traerTicketPorPrioridad(idPrioridad);
    	Optional<Prioridad> prioridadOpt = prioridadService.traer(idPrioridad);
    	
    	if(prioridadOpt.isPresent()) {
    		nombrePrioridadBuscado = prioridadOpt.get().getNombrePrioridad();
    	}
    	else {
    		nombrePrioridadBuscado = "ID de Prioridad " + idPrioridad + " (desconocido)";
    	}
    	
//        if(idPrioridad != null) {
//        	(aca va todo el desarrollo de arriba si puede estar vacio)
//        }else {
//        	nombrePrioridadBuscado = "Ningún Prioridad seleccionado";
//        }
    	
        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Prioridad: " + nombrePrioridadBuscado);
        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    @GetMapping("/tickets/buscar/fecha")
    public String buscarTicketsPorFecha(@RequestParam("fecha") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fecha, Model model) {
        List<Ticket> resultados = ticketService.findByFechaCreacion(fecha);

        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Fecha de creación: " + fecha.toString());

        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }

    @GetMapping("/tickets/buscar/fechas")
    public String buscarTicketsEntreFechas(
            @RequestParam("desde") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaDesde,
            @RequestParam("hasta") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fechaHasta,
            Model model) {

        List<Ticket> resultados = ticketService.findByFechaCreacionBetween(fechaDesde, fechaHasta);

        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Entre " + fechaDesde + " y " + fechaHasta);

        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    @GetMapping("/tickets/buscar/titulo")
    public String buscarTicketsPorTitulo(@RequestParam("valor") String textoTitulo, Model model) {
        List<Ticket> resultados = ticketService.traerPorNombreUsuarioConteniendo(textoTitulo);

        model.addAttribute("resultadosTickets", resultados);
        model.addAttribute("criterioBusqueda", "Título contiene: \"" + textoTitulo + "\"");

        return ViewRouteHelper.TICKETS_SEARCH_RESULTS;
    }
    
    

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("tickets/admin")
    public ModelAndView panelTicketsAdminConModelAndView() {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_TICKET_PANEL);
        List<Ticket> todosLosTickets = ticketService.traerTodos();
        mav.addObject("tickets", todosLosTickets);
        mav.addObject("clientes", usuarioService.traerPorRol(1L));
        mav.addObject("prioridades", prioridadService.traerTodas());
        mav.addObject("estados", estadoService.traerTodas());
        mav.addObject("tipos", tipoService.traerTodas());
        mav.addObject("categoriasDisponibles", categoriaService.traerTodas());
        return mav;
    }
}