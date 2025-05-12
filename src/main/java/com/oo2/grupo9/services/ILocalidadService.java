package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Localidad;
import java.util.List;
import java.util.Optional;

public interface ILocalidadService {
    List<Localidad> traerTodas();
    Optional<Localidad> traerPorId(Long id);
    Optional<Localidad> traerPorNombre(String nombreLocalidad);
}