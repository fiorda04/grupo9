package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Estado;
import java.util.List;
import java.util.Optional;

public interface IEstadoService {
    Optional<Estado> traer(Long id);
    List<Estado> traerTodas();
}
