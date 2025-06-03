package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Rol;
import com.oo2.grupo9.repositories.RolRepository; 
import com.oo2.grupo9.services.IRolService;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service("rolService")
public class RolService implements IRolService{
    private final RolRepository rolRepository; 

    public RolService(RolRepository rolRepository) { 
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Rol> traerTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> traerPorId(Long idRol) {
        return rolRepository.findById(idRol);
    }

    @Override
    public Optional<Rol> traerPorNombreRol(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol);
    }
}
