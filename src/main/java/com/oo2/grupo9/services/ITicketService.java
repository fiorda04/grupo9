package com.oo2.grupo9.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.oo2.grupo9.dtos.CrearTicketRequest;
import com.oo2.grupo9.dtos.CrearTicketResponse;
import com.oo2.grupo9.dtos.IntervencionDTO;
import com.oo2.grupo9.dtos.TicketResponseDTO;
import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Usuario;

public interface ITicketService {


    long agregar(String titulo, String descripcion, List<Categoria> lstCategorias, Long idPrioridad,
                LocalDateTime fechaCreacion, LocalDateTime fechaCierre, Long idEstado, Long idTipo, Long idUsuarioCliente,
                List<Intervencion> lstIntervenciones);


    void guardar(Ticket ticket);

    void modificar(Ticket ticket);

    void eliminar(long idTicket);

    Ticket traer(long idTicket);

    List<Ticket> traerTodos();

    List<Ticket> traerTicketsPorTipo(long idTipo);

    List<Ticket> traerPorCliente(long idUsuario);
    
    List<Ticket> traerPorCategoria(long idCategoria);

    List<Ticket> traerPorEmpleado(long idAutor); // se fija en las intervenciones

    List<Ticket> traerTicketPorEstado(long idEstado);

    List<Ticket> traerTicketPorPrioridad(long idPrioridad);

    List<Ticket> findByFechaCreacion(LocalDate fecha); 

    List<Ticket> findByFechaCreacionBetween(LocalDateTime fechaCreacion1, LocalDateTime fechaCreacion2); 
    
    List<Ticket> findByFechaCierreBetween(LocalDateTime fechaCreacion1, LocalDateTime fechaCreacion2); 
    
    List<Ticket> traerPorTituloConteniendo(String titulo);

	List<Ticket> traerTicketsConIntervencionesDeEmpleado(Usuario empleado);

	List<Intervencion> traerIntervencionesPorEmpleado(Long idEmpleado);

	void realizarIntervencion(IntervencionDTO dto);
	
	List<Ticket> buscarTicketsConFiltros(String titulo, Long categoriaId, Long idPrioridad, Long idEstado, Long idTipo,
            LocalDate fechaCreacionDesde, LocalDate fechaCreacionHasta, LocalDate fechaCierreDesde, LocalDate fechaCierreHasta,
            Usuario usuario);
	
	CrearTicketResponse crearTicketDesdeRequest(CrearTicketRequest request, String nombreUsuario) throws Exception;
	
	CrearTicketResponse traerResponse(Long id) throws Exception;

	TicketResponseDTO convertirADTO(Ticket ticket);
}