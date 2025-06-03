package com.oo2.grupo9.services;

import com.oo2.grupo9.entities.Rol;
import java.util.List;
import java.util.Optional;

public interface IRolService {
    List<Rol> traerTodos(); 
    Optional<Rol> traerPorId(Long idRol); 
    Optional<Rol> traerPorNombreRol(String nombreRol);
}
