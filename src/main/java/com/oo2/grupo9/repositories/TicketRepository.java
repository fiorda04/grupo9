package com.oo2.grupo9.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo9.entities.Ticket;

@Repository("ticketRepository")
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	// 17. +traer(long idTicket): Ticket
	Optional<Ticket> findByIdTicket(Long idTicket);

	// 18. +traerTicketsPorTipo(Tipo t): List<Ticket>
	List<Ticket> findByTipo_IdTipo(Long idTipo);

	// 27. +traerPorCliente(Usuario cliente): List<Ticket>
	List<Ticket> findByUsuarioCliente_IdUsuario(Long idUsuario);

	// 28. +traerPorEmpleado(Usuario empleado): List<Ticket>
	//List<Ticket> findByUsuarioCliente_Id(Long idAutor); // se fija en las intervenciones

	// 29. +traerTicketPorEstado(Estado estado): List<Ticket>
	List<Ticket> findByEstado_IdEstado(Long idEstado);

	// 30. +traerTicketPorPrioridad(Prioridad prioridad): List<Ticket>
	List<Ticket> findByPrioridad_IdPrioridad(Long idPrioridad);

	// 31. +traerPorFecha(LocalDate fecha): List<Ticket>
	List<Ticket> findByFechaCreacion(LocalDate fecha);
	
	//32. +traerTicketEntreFechas(LocalDate fechaInicio, LocalDate fechaFin): List<Ticket> 
	List<Ticket> findByFechaCreacionBetween(LocalDate inicio, LocalDate fin);


}