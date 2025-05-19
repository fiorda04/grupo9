package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Categoria;
import java.util.List;

public interface ICategoriaService {
    Categoria traer(Long id);
    List<Categoria> traerTodas();
    
}