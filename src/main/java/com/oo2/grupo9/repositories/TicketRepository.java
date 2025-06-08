package com.oo2.grupo9.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oo2.grupo9.entities.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {


	Optional<Ticket> findByIdTicket(Long idTicket);


	List<Ticket> findByTipo_IdTipo(Long idTipo);
	List<Ticket> findByUsuarioCliente_IdUsuario(Long idUsuario);
	List<Ticket> findDistinctByLstIntervenciones_Autor_idUsuario(Long idEmpleado);// se fija en las intervenciones y solo agarra un ticket por intervencion
	List<Ticket> findByEstado_IdEstado(Long idEstado);
	List<Ticket> findByLstCategorias_idCategoria(Long idCategoria);
	List<Ticket> findByPrioridad_IdPrioridad(Long idPrioridad);

    @Query("SELECT t FROM Ticket t WHERE t.fechaCreacion = :fecha")
    List<Ticket> findByFechaCreacion(@Param("fecha") LocalDate fecha);

    @Query("SELECT t FROM Ticket t WHERE t.fechaCreacion BETWEEN :fechaInicio AND :fechaFin")
    List<Ticket> findByFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT t FROM Ticket t WHERE t.fechaCierre BETWEEN :fechaInicio AND :fechaFin")
    List<Ticket> findByFechaCierreBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    
    List<Ticket> findByTituloContainingIgnoreCase(String titulo);

}