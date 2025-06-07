package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Intervencion;
import java.time.LocalDate;
import java.util.List;

public interface IIntervencionService {
    List<Intervencion> traerTodas();
    List<Intervencion> traerPorTicketId(Long ticketId);
    List<Intervencion> traerPorEmpleadoId(Long empleadoId);
    List<Intervencion> traerPorContenido(String contenido);
    List<Intervencion> traerPorFechas(LocalDate desde, LocalDate hasta);
    void eliminar(Long idIntervencion);
}