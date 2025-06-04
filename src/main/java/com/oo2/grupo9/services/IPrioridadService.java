package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Prioridad;
import java.util.List;
import java.util.Optional;

public interface IPrioridadService {
    Optional<Prioridad> traer(Long id);
    List<Prioridad> traerTodas();
}