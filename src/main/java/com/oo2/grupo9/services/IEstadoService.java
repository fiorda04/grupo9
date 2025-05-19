package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Estado;
import java.util.List;

public interface IEstadoService {
    Estado traer(Long id);
    List<Estado> traerTodas();
}
