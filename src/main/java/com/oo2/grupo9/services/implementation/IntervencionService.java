package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Intervencion;
import com.oo2.grupo9.repositories.IntervencionRepository;
import com.oo2.grupo9.services.IIntervencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class IntervencionService implements IIntervencionService{
    @Autowired
    private IntervencionRepository intervencionRepository;

    @Override
    public List<Intervencion> traerTodas() {
        return intervencionRepository.findAll();
    }
    @Override
    public List<Intervencion> traerPorTicketId(Long ticketId) {
        return intervencionRepository.findByTicket_IdTicket(ticketId);
    }
    @Override
    public List<Intervencion> traerPorEmpleadoId(Long empleadoId) {
        return intervencionRepository.findByAutor_IdUsuario(empleadoId);
    }
    @Override
    public List<Intervencion> traerPorContenido(String contenido) {
        return intervencionRepository.findByContenidoContainingIgnoreCase(contenido);
    }
    @Override
    public List<Intervencion> traerPorFechas(LocalDate desde, LocalDate hasta) {
        return intervencionRepository.findByfechaIntervencionBetween(desde.atStartOfDay(), hasta.atTime(LocalTime.MAX));
    }
}
