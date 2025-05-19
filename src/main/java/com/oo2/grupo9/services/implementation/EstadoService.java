package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Estado;
import com.oo2.grupo9.repositories.EstadoRepository;
import com.oo2.grupo9.services.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoService implements IEstadoService {

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public Estado traer(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el estado con ID: " + id));
    }

    @Override
    public List<Estado> traerTodas() {
        return estadoRepository.findAll();
    }
}