package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Prioridad;
import com.oo2.grupo9.repositories.PrioridadRepository;
import com.oo2.grupo9.services.IPrioridadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrioridadService implements IPrioridadService {

    @Autowired
    private PrioridadRepository prioridadRepository;

    @Override
    public Optional<Prioridad> traer(Long id) {
        return prioridadRepository.findById(id);
    }

    @Override
    public List<Prioridad> traerTodas() {
        return prioridadRepository.findAll();
    }
}
