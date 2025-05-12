package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Localidad;
import com.oo2.grupo9.services.ILocalidadService;
import com.oo2.grupo9.repositories.LocalidadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalidadService implements ILocalidadService {

    private final LocalidadRepository localidadRepository;

    public LocalidadService(LocalidadRepository localidadRepository) {
        this.localidadRepository = localidadRepository;
    }

    @Override
    public List<Localidad> traerTodas() {
        return localidadRepository.findAll();
    }

    @Override
    public Optional<Localidad> traerPorId(Long id) {
        return localidadRepository.findById(id);
    }

    @Override
    public Optional<Localidad> traerPorNombre(String nombreLocalidad) {
        return localidadRepository.findByNombreLocalidad(nombreLocalidad).stream().findFirst();
    }

}
