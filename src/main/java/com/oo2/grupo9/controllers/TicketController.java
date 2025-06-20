package com.oo2.grupo9.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.oo2.grupo9.dtos.IntervencionDTO;
import com.oo2.grupo9.dtos.TicketCreacionDTO;
import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Estado;
import com.oo2.grupo9.entities.Prioridad;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Tipo;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.exceptions.CategoriaNoValidaException;
import com.oo2.grupo9.exceptions.ClienteNoEncontradoException;
import com.oo2.grupo9.exceptions.EmpleadoNoEncontradoException;
import com.oo2.grupo9.exceptions.RangoDeFechasInvalidoException;
import com.oo2.grupo9.exceptions.CampoBusquedaVacioException;
import com.oo2.grupo9.helpers.ViewRouteHelper;
import com.oo2.grupo9.services.ICategoriaService;
import com.oo2.grupo9.services.IEstadoService;
import com.oo2.grupo9.services.IPrioridadService;
import com.oo2.grupo9.services.ITicketService;
import com.oo2.grupo9.services.ITipoService;
import com.oo2.grupo9.services.IUsuarioService;

import jakarta.validation.Valid;


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
    

    //Franco Romay
    @GetMapping("/tickets/crear")
    public ModelAndView mostrarFormularioCrearTicket() {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKET_CREAR);
        mAV.addObject("nuevoTicketDTO", new TicketCreacionDTO()); // Pasa el nuevo DTO vacío
        mAV.addObject("categoriasDisponibles", categoriaService.traerTodas());
        mAV.addObject("tiposDisponibles", tipoService.traerTodas());
        return mAV;
    }

    //Franco Romay
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
            nuevoTicket.setFechaCreacion(LocalDateTime.now()); 
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
    
    //Franco Romay
    @GetMapping("tickets/VerTicket/{id}")
    public ModelAndView verTicket(@PathVariable("id") Long id) {
        ModelAndView mAV = new ModelAndView(ViewRouteHelper.VER_TICKET);
        Ticket ticket = ticketService.traer(id);

        IntervencionDTO intervencionDTO = new IntervencionDTO();
        intervencionDTO.setTicketId(id); // esto es necesario para que el formulario lo use

        mAV.addObject("ticket", ticket);
        mAV.addObject("intervencionDTO", intervencionDTO);
        mAV.addObject("estadosDisponibles", estadoService.traerTodas());
        mAV.addObject("prioridadesDisponibles", prioridadService.traerTodas());

        return mAV;
    }
    
    @PostMapping("/intervenciones/agregar")
    public String agregarIntervencion(@ModelAttribute("intervencionDTO") IntervencionDTO dto) {
        ticketService.realizarIntervencion(dto);

        return "redirect:/tickets/VerTicket/" + dto.getTicketId();
    }
    
    //Franco Romay
    @PreAuthorize("hasRole('ROLE_Admin')")
    @PostMapping("tickets/eliminar/{id}")
    public String eliminarTicket(@PathVariable("id") Long id) {
        ticketService.eliminar(id);
        return  ViewRouteHelper.ADMIN_TICKET_PANEL;    
    }
    
    @GetMapping("/tickets/buscar/tipo")
    public ModelAndView buscarTicketsPorTipo(@RequestParam("valor") Long idTipo) {
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
        List<Ticket> resultados = new ArrayList<>();
        String nombreTipoBuscado;
        
        resultados = ticketService.traerTicketsPorTipo(idTipo);
    	Optional<Tipo> tipoOpt = tipoService.traer(idTipo);
    	if(tipoOpt.isPresent()) {
    		nombreTipoBuscado = tipoOpt.get().getNombreTipo();
    	}else {
    		nombreTipoBuscado = "ID de Tipo " + idTipo + " (desconocido)";
    	}
        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Tipo: " + nombreTipoBuscado);
        return mAV;
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/tickets/buscar/cliente")
    public ModelAndView buscarTicketsPorCliente(@RequestParam("valor") Long idUsuario) {
    	if(usuarioService.traer(idUsuario) == null)throw new ClienteNoEncontradoException("No se encontró el usuario con ID: " + idUsuario);
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
        List<Ticket> resultados = ticketService.traerPorCliente(idUsuario);
        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Usuario: " + usuarioService.traer(idUsuario).getNombreUsuario());
        return mAV;
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/tickets/buscar/categoria")
    public ModelAndView buscarTicketsPorCategoria(@RequestParam("valor") Long idCategoria) {
    	if(categoriaService.traer(idCategoria) == null) throw new CategoriaNoValidaException("La categoria: " + categoriaService.traer(idCategoria).getNombreCategoria() + "no es valida.");
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
        List<Ticket> resultados = ticketService.traerPorCategoria(idCategoria);
        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Categoria: " + categoriaService.traer(idCategoria).getNombreCategoria());
        return mAV;
    }
    
    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("/tickets/buscar/empleado")
    public ModelAndView buscarTicketsPorEmpleado(@RequestParam("valor") Long idAutor) {
    	if(usuarioService.traer(idAutor) == null)throw new EmpleadoNoEncontradoException("No se encontró el usuario con ID: " + idAutor);
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
        List<Ticket> resultados = ticketService.traerPorEmpleado(idAutor);
        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Usuario: " + usuarioService.traer(idAutor).getNombreUsuario());
        return mAV;
    }
    
    @GetMapping("/tickets/buscar/estado")
    public ModelAndView buscarTicketsPorEstado(@RequestParam("valor") Long idEstado) {
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
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
        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Estado: " + nombreEstadoBuscado);
        return mAV;
    }
    
    @GetMapping("/tickets/buscar/prioridad")
    public ModelAndView buscarTicketsPorPrioridad(@RequestParam("valor") Long idPrioridad) {
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
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
        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Prioridad: " + nombrePrioridadBuscado);
        return mAV;
    }
    
    @GetMapping("/tickets/buscar/fecha")
    public ModelAndView buscarTicketsPorFecha(@RequestParam("fecha") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fecha) {
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
        List<Ticket> resultados = ticketService.findByFechaCreacion(fecha);

        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Fecha de creación: " + fecha.toString());

        return mAV;
    }

    @GetMapping("/tickets/buscar/fechas")
    public ModelAndView buscarTicketsEntreFechas(
            @RequestParam("fechaDesde") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaDesde,
            @RequestParam("fechaHasta") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaHasta) {
    	if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta))
    	    throw new RangoDeFechasInvalidoException("La fecha inicial no puede ser posterior a la final.");
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);

    	LocalDateTime desdeInicio = fechaDesde.atStartOfDay();
    	LocalDateTime hastaFin = fechaHasta.atTime(23,59,59);
    	
        List<Ticket> resultados = ticketService.findByFechaCreacionBetween(desdeInicio, hastaFin);

        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Entre " + fechaDesde + " y " + fechaHasta);

        return mAV;
    }
    
    @GetMapping("/tickets/buscar/fechasCierre")
    public ModelAndView buscarTicketsEntreFechasCierre(
            @RequestParam("fechaDesde") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaDesde,
            @RequestParam("fechaHasta") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaHasta) {
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);

    	LocalDateTime desdeInicio = fechaDesde.atStartOfDay();
    	LocalDateTime hastaFin = fechaHasta.atTime(23,59,59);
    	
        List<Ticket> resultados = ticketService.findByFechaCierreBetween(desdeInicio, hastaFin);

        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Entre " + fechaDesde + " y " + fechaHasta);

        return mAV;
    }
    
    @GetMapping("/tickets/buscar/titulo")
    public ModelAndView buscarTicketsPorTitulo(@RequestParam("valor") String textoTitulo) {
    	ModelAndView mAV = new ModelAndView(ViewRouteHelper.TICKETS_SEARCH_RESULTS);
        List<Ticket> resultados = ticketService.traerPorTituloConteniendo(textoTitulo);

        mAV.addObject("resultadosTickets", resultados);
        mAV.addObject("criterioBusqueda", "Título contiene: \"" + textoTitulo + "\"");

        return mAV;
    }
    
    

    @PreAuthorize("hasRole('ROLE_Admin')")
    @GetMapping("tickets/admin")
    public ModelAndView panelTicketsAdminConModelAndView() {
        ModelAndView mav = new ModelAndView(ViewRouteHelper.ADMIN_TICKET_PANEL);
        List<Ticket> todosLosTickets = ticketService.traerTodos();
        mav.addObject("tickets", todosLosTickets);
        mav.addObject("clientes", usuarioService.traerPorRol(1L));
        mav.addObject("empleados", usuarioService.traerPorRol(2L));
        mav.addObject("prioridades", prioridadService.traerTodas());
        mav.addObject("estados", estadoService.traerTodas());
        mav.addObject("tipos", tipoService.traerTodas());
        mav.addObject("categoriasDisponibles", categoriaService.traerTodas());
        return mav;
    }
    
    @GetMapping("/tickets/mis-tickets")
    public ModelAndView mostrarFormularioMisTickets() {
        ModelAndView mAV = new ModelAndView("tickets/mis-tickets"); 

        mAV.addObject("categoriasDisponibles", categoriaService.traerTodas());
        mAV.addObject("prioridadesDisponibles", prioridadService.traerTodas());
        mAV.addObject("estadosDisponibles", estadoService.traerTodas());
        mAV.addObject("tiposDisponibles", tipoService.traerTodas());

        return mAV;
    }

    @GetMapping("/tickets/ResultadoBusquedaTicketsCliente")
    public ModelAndView buscarTicketsConFiltros(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long prioridadId,
            @RequestParam(required = false) Long estadoId,
            @RequestParam(required = false) Long tipoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCreacionDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCreacionHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCierreDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaCierreHasta
    ) {
        ModelAndView mAV = new ModelAndView("/tickets/ResultadosBusquedaTicketsCliente"); 

        if ((titulo == null || titulo.isBlank()) && categoriaId == null && prioridadId == null &&
                estadoId == null && tipoId == null &&
                fechaCreacionDesde == null && fechaCreacionHasta == null &&
                fechaCierreDesde == null && fechaCierreHasta == null) {
                
                throw new CampoBusquedaVacioException("Por favor, selecciona al menos un campo para realizar la busqueda");
            }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario cliente = usuarioService.traer(username);

        List<Ticket> tickets = ticketService.buscarTicketsConFiltros(
            titulo, categoriaId, prioridadId, estadoId, tipoId, 
            fechaCreacionDesde, fechaCreacionHasta, fechaCierreDesde, fechaCierreHasta, cliente
        );

        mAV.addObject("resultadosTickets", tickets);

        mAV.addObject("categoriasDisponibles", categoriaService.traerTodas());
        mAV.addObject("prioridadesDisponibles", prioridadService.traerTodas());
        mAV.addObject("estadosDisponibles", estadoService.traerTodas());
        mAV.addObject("tiposDisponibles", tipoService.traerTodas());

        mAV.addObject("titulo", titulo);
        mAV.addObject("categoriaId", categoriaId);
        mAV.addObject("prioridadId", prioridadId);
        mAV.addObject("estadoId", estadoId);
        mAV.addObject("tipoId", tipoId);
        mAV.addObject("fechaCreacionDesde", fechaCreacionDesde);
        mAV.addObject("fechaCreacionHasta", fechaCreacionHasta);
        mAV.addObject("fechaCierreDesde", fechaCierreDesde);
        mAV.addObject("fechaCierreHasta", fechaCierreHasta);

        return mAV;
    }
}