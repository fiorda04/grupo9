package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Tipo;
import java.util.List;

public interface ITipoService {
    Tipo traer(Long id);
    List<Tipo> traerTodas();
}