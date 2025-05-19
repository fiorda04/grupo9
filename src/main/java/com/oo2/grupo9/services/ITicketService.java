package com.oo2.grupo9.services;

import java.time.LocalDate;
import java.util.List;
import com.oo2.grupo9.entities.Ticket;

public interface ITicketService {

	long agregar(String titulo, String descripcion, List<Long> categoriasId, Long idTipo, Long idEstado,
			Long idPrioridad, Long idUsuario);

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
