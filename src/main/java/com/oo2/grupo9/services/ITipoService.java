package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Tipo;
import java.util.List;
import java.util.Optional;

public interface ITipoService {
    Optional<Tipo> traer(Long id);
    List<Tipo> traerTodas();
}