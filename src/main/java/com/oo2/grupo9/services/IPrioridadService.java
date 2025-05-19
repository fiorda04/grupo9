package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Prioridad;
import java.util.List;

public interface IPrioridadService {
    Prioridad traer(Long id);
    List<Prioridad> traerTodas();
}