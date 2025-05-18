package com.oo2.grupo9.services;

import java.time.LocalDate;
import java.util.List;

import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.entities.Intervencion;

public interface ITicketService {

	long agregar(String titulo, String descripcion, List<Categoria> lstCategorias, Long idPrioridad,
			LocalDate fechaCreacion, LocalDate fechaCierre, Long idEstado, Long idTipo, Long idUsuarioCliente,
			List<Intervencion> lstIntervenciones);

	void modificar(Ticket ticket);

	void eliminar(long idTicket);
	
	Ticket traer(long idTicket);
	
	List<Ticket> traerTodos();

	List<Ticket> traerTicketsPorTipo(long idTipo);

	List<Ticket> traerPorCliente(long idUsuario);

	List<Ticket> traerPorEmpleado(long idAutor); // se fija en las intervenciones

	List<Ticket> traerTicketPorEstado(long idEstado);

	List<Ticket> traerTicketPorPrioridad(long idPrioridad);

	List<Ticket> traerPorFecha(LocalDate fecha);
	
	List<Ticket> traerTicketEntreFechas(LocalDate fechaCreacion1, LocalDate fechaCreacion2);
	
	
}
