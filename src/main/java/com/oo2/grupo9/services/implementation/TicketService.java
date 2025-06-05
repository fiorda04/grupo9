package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.exceptions.TicketNoEncontradoException;
import com.oo2.grupo9.repositories.TicketRepository;
import com.oo2.grupo9.services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService implements ITicketService {

    private final AuthenticationManager authenticationManager;

    @Autowired
    private TicketRepository ticketRepository;


    TicketService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    
    @Override 
    public long agregar(String titulo, String descripcion, List<Categoria> lstCategorias, Long idPrioridad,
                        LocalDate fechaCreacion, LocalDate fechaCierre, Long idEstado, Long idTipo, Long idUsuarioCliente,
                        List<Intervencion> lstIntervenciones) {
        // Este método ya no lo utilizaremos directamente en el flujo de "guardar" del formulario
        // pero lo mantenemos por si tiene otros usos.
        Ticket nuevoTicket = new Ticket();
        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setDescripcion(descripcion);
        nuevoTicket.setLstCategorias(lstCategorias);
        // Necesitarías cargar las entidades Prioridad, Estado, Tipo y UsuarioCliente por sus IDs
        // y setearlas aquí.
        // nuevoTicket.setPrioridad(...);
        // nuevoTicket.setEstado(...);
        // nuevoTicket.setTipo(...);
        // nuevoTicket.setUsuarioCliente(...);
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
    public Optional<Ticket> traer(long idTicket) {
        return ticketRepository.findById(idTicket);
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
    public List<Ticket> traerPorEmpleado(long idAutor) {
        // Esto requeriría una consulta más compleja, posiblemente a través de Intervenciones
        // o si la entidad Ticket tiene un campo para el empleado asignado.
        // Por ahora, devolvemos una lista vacía o podrías lanzar una excepción si no está implementado.
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
    public List<Ticket> findByFechaCreacionBetween(LocalDate fechaCreacion1, LocalDate fechaCreacion2) {
        return ticketRepository.findByFechaCreacionBetween(fechaCreacion1, fechaCreacion2);
    }
    
    @Override
    public List<Ticket> traerPorTituloConteniendo(String titulo){
    	return ticketRepository.findByTituloContainingIgnoreCase(titulo.trim());
    }
}

