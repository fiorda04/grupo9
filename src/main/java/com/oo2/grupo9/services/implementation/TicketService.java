package com.oo2.grupo9.services.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.oo2.grupo9.Grupo9Application;
import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Contacto;
import com.oo2.grupo9.entities.Estado;
import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.entities.Prioridad;
import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Tipo;
import com.oo2.grupo9.entities.Usuario;
import com.oo2.grupo9.repositories.CategoriaRepository;
import com.oo2.grupo9.repositories.EstadoRepository;
import com.oo2.grupo9.repositories.PrioridadRepository;
import com.oo2.grupo9.repositories.TicketRepository;
import com.oo2.grupo9.repositories.TipoRepository;
import com.oo2.grupo9.repositories.UsuarioRepository;
import com.oo2.grupo9.services.ITicketService;

import jakarta.transaction.Transactional;

@Service("ticketService")
@Transactional
public class TicketService implements ITicketService{

	
	@Autowired
	private TipoRepository tipoRepository;
	private EstadoRepository estadoRepository;
	private PrioridadRepository prioridadRepository;
	private TicketRepository ticketRepository;
	private UsuarioRepository usuarioRepository;
	private CategoriaRepository categoriaRepository;
	
    public TicketService(
        TipoRepository tipoRepository,
        EstadoRepository estadoRepository,
        PrioridadRepository prioridadRepository,
        TicketRepository ticketRepository,
        UsuarioRepository usuarioRepository, Grupo9Application grupo9Application) {
        this.tipoRepository = tipoRepository;
        this.estadoRepository = estadoRepository;
        this.prioridadRepository = prioridadRepository;
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
        this.grupo9Application = grupo9Application;
    }
	
	@Override
    public long agregar(String titulo, String descripcion, List<Long> categoriasId, Long idTipo, Long idEstado, Long idPrioridad, Long idUsuario) {

        Ticket nuevoTicket = new Ticket();

        nuevoTicket.setTitulo(titulo);
        nuevoTicket.setDescripcion(descripcion);
        
        List<Categoria> categorias = new ArrayList<Categoria>();
        for (Long idCategoria : categoriasId) {
            Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + idCategoria));
            categorias.add(categoria);
        }
        nuevoTicket.setLstCategorias(categorias);


        Optional<Tipo> tipoOptional = tipoRepository.findById(idTipo);
        if (tipoOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el tipo con ID: " + idTipo);
        }
        nuevoTicket.setTipo(tipoOptional.get());
        
        Optional<Estado> estadoOptional = estadoRepository.findById(idEstado);
        if (estadoOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el estado con ID: " + idEstado);
        }
        nuevoTicket.setEstado(estadoOptional.get());
        
        Optional<Prioridad> prioridadOptional = prioridadRepository.findById(idPrioridad);
        if (prioridadOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro la prioridad con ID: " + idPrioridad);
        }
        nuevoTicket.setPrioridad(prioridadOptional.get());
        
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(idUsuario);
        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el usuario con ID: " + idUsuario);
        }
        nuevoTicket.setUsuarioCliente(usuarioOptional.get());

        return ticketRepository.save(nuevoTicket).getIdTicket();
    }
	
	@Override
    public void modificar(Ticket ticket) {
        Optional<Ticket> ticketExistenteOptional = ticketRepository.findById(ticket.getIdTicket());
        if (ticketExistenteOptional.isEmpty()) {
            throw new IllegalArgumentException("No existe un ticket con el ID proporcionado.");
        }
        Ticket ticketExistente = ticketExistenteOptional.get(); 
        
        Optional<Usuario> usuarioExistenteOptional = usuarioRepository.findById(ticket.getUsuarioCliente().getIdUsuario());
        if (usuarioExistenteOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el usuario con ID que se le puso al ticket: " + ticket.getUsuarioCliente().getIdUsuario());
        }
        
        Optional<Tipo> tipoExistenteOptional = tipoRepository.findById(ticket.getTipo().getIdTipo());
        if (tipoExistenteOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el tipo que se le puso al ticket: " + ticket.getTipo().getNombreTipo());
        }
        
        Optional<Estado> estadoExistenteOptional = estadoRepository.findById(ticket.getEstado().getIdEstado());
        if (estadoExistenteOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el estado que se le puso al ticket: " + ticket.getEstado().getNombreEstado());
        }
        
        Optional<Prioridad> prioridadExistenteOptional = prioridadRepository.findById(ticket.getPrioridad().getIdPrioridad());
        if (prioridadExistenteOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro la prioridad que se le puso al ticket: " + ticket.getPrioridad().getNombrePrioridad());
        }
        
        List<Categoria> categorias = ticket.getLstCategorias();
        for (Categoria c : categorias) {
            Optional<Categoria> categoriaOptional = categoriaRepository.findById(c.getIdCategoria());
            if(categoriaOptional.isEmpty()) {
            	throw new IllegalArgumentException("Categoría no encontrada: " + c.getNombreCategoria());
            }
        }

        ticketExistente.setTitulo(ticket.getTitulo());
        ticketExistente.setDescripcion(ticket.getDescripcion());
        ticketExistente.setLstCategorias(ticket.getLstCategorias());
        ticketExistente.setPrioridad(ticket.getPrioridad());
        ticketExistente.setFechaCreacion(ticket.getFechaCreacion());;
        ticketExistente.setFechaCierre(ticket.getFechaCierre());
        ticketExistente.setEstado(ticket.getEstado());
        ticketExistente.setTipo(ticket.getTipo());
        ticketExistente.setUsuarioCliente(ticket.getUsuarioCliente());

        ticketRepository.save(ticketExistente);
    }
	
	public void eliminar(Long id) {
        try {
            Optional<Ticket> ticketOptional = ticketRepository.findById(id);
            if (ticketOptional.isEmpty()) {
                throw new IllegalArgumentException("No existe un ticket con el ID proporcionado.");
            }
            Ticket ticketExistente = ticketOptional.get();
            ticketRepository.delete(ticketExistente);//Elimina las intervenciones del ticket por cascada :D
        } catch (Exception e) {
            System.err.println("¡ERROR EN EL SERVICIO AL ELIMINAR TICKET =(!");
            e.printStackTrace();
            throw e;
        }
    }
	
	public Ticket traer(long idTicket) {
		return ticketRepository.findById(idTicket)
				.orElseThrow(() -> new IllegalArgumentException("No se encontro un ticket con el ID proporcionado."));
	}
	
	public List<Ticket> traerTodos(){
		return ticketRepository.findAll();
	}
	
	public List<Ticket> traerTicketsPorTipo(long idTipo){
		Optional<Tipo> tipoOptional = tipoRepository.findById(idTipo);
		if (tipoOptional.isEmpty()) {
            throw new IllegalArgumentException("No se encontro el tipo con el ID proporcionado.");
        }
		return ticketRepository.findByTipo_IdTipo(idTipo);
	}
	
	public List<Ticket> traerTicketPorPrioridad(long idPrioridad){
		Optional<Prioridad> prioridadOptional = prioridadRepository.findById(idPrioridad);
		if(prioridadOptional.isEmpty()) {
			throw new IllegalAccessError("No se encontro la prioridad con el ID proporcionado.");
		}
		return ticketRepository.findByPrioridad_IdPrioridad(idPrioridad);
	}
	
	
}
