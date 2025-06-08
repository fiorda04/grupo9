package com.oo2.grupo9.services.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oo2.grupo9.dtos.IntervencionDTO;
import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Estado;
import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.entities.Prioridad;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.exceptions.TicketCerradoException;
import com.oo2.grupo9.exceptions.TicketNoEncontradoException;
import com.oo2.grupo9.repositories.EstadoRepository;
import com.oo2.grupo9.repositories.IntervencionRepository;
import com.oo2.grupo9.repositories.PrioridadRepository;
import com.oo2.grupo9.repositories.TicketRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;
import com.oo2.grupo9.services.ITicketService;

@Service
@Transactional
public class TicketService implements ITicketService {


    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private IntervencionRepository intervencionRepository;

    
    @Override 
    public long agregar(String titulo, String descripcion, List<Categoria> lstCategorias, Long idPrioridad,
                        LocalDateTime fechaCreacion, LocalDateTime fechaCierre, Long idEstado, Long idTipo, Long idUsuarioCliente,
                        List<Intervencion> lstIntervenciones) {
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setDescripcion(descripcion);
        nuevoTicket.setLstCategorias(lstCategorias);
        nuevoTicket.setFechaCreacion(fechaCreacion);
        nuevoTicket.setFechaCierre(fechaCierre);
        nuevoTicket.setLstIntervenciones(lstIntervenciones);
        return ticketRepository.save(nuevoTicket).getIdTicket();
    }
    @Override
    public void guardar(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void modificar(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    @Override
    public void eliminar(long idTicket) {
        ticketRepository.deleteById(idTicket);
    }

    @Override
    public Ticket traer(long idTicket) {
        return ticketRepository.findById(idTicket).orElseThrow(() -> new TicketNoEncontradoException("No se encontró el ticket con ID: " + idTicket));
    }

    @Override
    public List<Ticket> traerTodos() {
        return ticketRepository.findAll();
        		
    }

    @Override
    public List<Ticket> traerTicketsPorTipo(long idTipo) {
        return ticketRepository.findByTipo_IdTipo(idTipo);
    }

    @Override
    public List<Ticket> traerPorCliente(long idUsuario) {
        return ticketRepository.findByUsuarioCliente_IdUsuario(idUsuario);
    }

    @Override
    public List<Ticket> traerPorCategoria(long idCategoria) {
        return ticketRepository.findByLstCategorias_idCategoria(idCategoria);
    }
    
    @Override
    public List<Ticket> traerPorEmpleado(long idAutor) {
        return ticketRepository.findDistinctByLstIntervenciones_Autor_idUsuario(idAutor);
    }

    @Override
    public List<Ticket> traerTicketPorEstado(long idEstado) {
        return ticketRepository.findByEstado_IdEstado(idEstado);
    }

    @Override
    public List<Ticket> traerTicketPorPrioridad(long idPrioridad) {
        return ticketRepository.findByPrioridad_IdPrioridad(idPrioridad);
    }

    @Override
    public List<Ticket> findByFechaCreacion(LocalDate fecha) {
        return ticketRepository.findByFechaCreacion(fecha);
    }

    @Override
    public List<Ticket> findByFechaCreacionBetween(LocalDateTime fechaCreacion1, LocalDateTime fechaCreacion2) {
        return ticketRepository.findByFechaCreacionBetween(fechaCreacion1, fechaCreacion2);
    }
    
    @Override
    public List<Ticket> findByFechaCierreBetween(LocalDateTime fechaCreacion1, LocalDateTime fechaCreacion2) {
        return ticketRepository.findByFechaCreacionBetween(fechaCreacion1, fechaCreacion2);
    }
    
    @Override
    public List<Ticket> traerPorTituloConteniendo(String titulo){
    	return ticketRepository.findByTituloContainingIgnoreCase(titulo.trim());
    }
    
    
    @Override
    public List<Intervencion> traerIntervencionesPorEmpleado(Long idEmpleado) {
        return intervencionRepository.findByAutor_IdUsuario(idEmpleado);
    }
    
    
    @Override
    public List<Ticket> traerTicketsConIntervencionesDeEmpleado(Usuario empleado) {
        List<Ticket> todos = ticketRepository.findAll();

        return todos.stream()
            .filter(ticket -> ticket.getLstIntervenciones() != null &&
                              ticket.getLstIntervenciones().stream()
                                    .anyMatch(i -> i.getAutor() != null &&
                                                   i.getAutor().getIdUsuario() != null &&
                                                   i.getAutor().getIdUsuario().equals(empleado.getIdUsuario())))
            .collect(Collectors.toList());
    }
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private PrioridadRepository prioridadRepository;
    
    public void realizarIntervencion(IntervencionDTO dto) {
        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new TicketNoEncontradoException("Ticket no encontrado"));
        
        //Verificar si el ticket ya está "Cerrado" (idEstado = 6)
        if (ticket.getEstado().getIdEstado().equals(Estado.ID_ESTADO_CERRADO)) {
            throw new TicketCerradoException("No se puede intervenir un ticket que ya está cerrado.");
        }

        Intervencion intervencion = new Intervencion();
        intervencion.setTicket(ticket);
        intervencion.setContenido(dto.getContenido());
        intervencion.setFechaIntervencion(LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario empleado = usuarioRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        intervencion.setAutor(empleado);

        Estado nuevoEstado = estadoRepository.findById(dto.getEstadoId()).orElseThrow();
        Prioridad nuevaPrioridad = prioridadRepository.findById(dto.getPrioridadId()).orElseThrow();

        ticket.setEstado(nuevoEstado);
        ticket.setPrioridad(nuevaPrioridad);
        
        //Si el estado es "Cerrado" (idEstado = 6), actualizar la fecha de cierre
        if (nuevoEstado.getIdEstado().equals(Estado.ID_ESTADO_CERRADO)) {
            ticket.setFechaCierre(LocalDateTime.now());
        }
        intervencionRepository.save(intervencion);
        ticketRepository.save(ticket);
    }
    
    @Override
    public List<Ticket> buscarTicketsConFiltros(String titulo, Long categoriaId, Long idPrioridad, Long idEstado, Long idTipo,
    		LocalDate fechaCreacionDesde, LocalDate fechaCreacionHasta, LocalDate fechaCierreDesde, LocalDate fechaCierreHasta,
    		Usuario usuario) {
        List<Ticket> tickets = ticketRepository.findAll();

        if (titulo != null && !titulo.isEmpty()) {
            tickets.removeIf(ticket -> !ticket.getTitulo().toLowerCase().contains(titulo.toLowerCase()));
        }

        if (categoriaId != null) {
            tickets.removeIf(ticket -> ticket.getLstCategorias().stream()
                    .noneMatch(cat -> cat.getIdCategoria().equals(categoriaId)));
        }

        if (idPrioridad != null ) {
            tickets.removeIf(ticket -> ticket.getPrioridad() == null || !ticket.getPrioridad().getIdPrioridad().equals(idPrioridad));
        }
        
        if (idEstado != null ) {
            tickets.removeIf(ticket -> ticket.getEstado() == null || !ticket.getEstado().getIdEstado().equals(idEstado));
        }

        if (idTipo != null ) {
            tickets.removeIf(ticket -> ticket.getTipo() == null || !ticket.getTipo().getIdTipo().equals(idTipo));
        }

        if (fechaCreacionDesde != null) {
            tickets.removeIf(ticket -> ticket.getFechaCreacion() == null || ticket.getFechaCreacion().toLocalDate().isBefore(fechaCreacionDesde));
        }
        if (fechaCreacionHasta != null) {
            tickets.removeIf(ticket -> ticket.getFechaCreacion() == null || ticket.getFechaCreacion().toLocalDate().isAfter(fechaCreacionHasta));
        }

        if (fechaCierreDesde != null) {
            tickets.removeIf(ticket -> ticket.getFechaCierre() == null || ticket.getFechaCierre().toLocalDate().isBefore(fechaCierreDesde));
        }
        if (fechaCierreHasta != null) {
            tickets.removeIf(ticket -> ticket.getFechaCierre() == null || ticket.getFechaCierre().toLocalDate().isAfter(fechaCierreHasta));
        }

        if (usuario != null && usuario.getIdUsuario() != null) {
            tickets.removeIf(ticket -> ticket.getUsuarioCliente() == null || ticket.getUsuarioCliente().getIdUsuario() == null || !ticket.getUsuarioCliente().getIdUsuario().equals(usuario.getIdUsuario()));
        }

        return tickets;
    } 
}

