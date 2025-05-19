package com.oo2.grupo9.services;

import java.time.LocalDate;
import java.util.List;

import com.oo2.grupo9.entities.Ticket;
import com.oo2.grupo9.entities.Categoria;

public interface ITicketService {


    long agregar(String titulo, String descripcion, List<Categoria> lstCategorias, Long idPrioridad,
                LocalDate fechaCreacion, LocalDate fechaCierre, Long idEstado, Long idTipo, Long idUsuarioCliente,
                List<Intervencion> lstIntervenciones);


    void guardar(Ticket ticket);

    void modificar(Ticket ticket);

    void eliminar(long idTicket);

    Ticket traer(long idTicket);

    List<Ticket> traerTodos();

    List<Ticket> traerTicketsPorTipo(long idTipo);

    List<Ticket> traerPorCliente(long idUsuario);

    List<Ticket> traerPorEmpleado(long idAutor); // se fija en las intervenciones

    List<Ticket> traerTicketPorEstado(long idEstado);

    List<Ticket> traerTicketPorPrioridad(long idPrioridad);

    List<Ticket> findByFechaCreacion(LocalDate fecha); // ¡Asegúrate de que el nombre sea EXACTAMENTE este!

    List<Ticket> findByFechaCreacionBetween(LocalDate fechaCreacion1, LocalDate fechaCreacion2); // ¡Asegúrate de que el nombre sea EXACTAMENTE este!
}