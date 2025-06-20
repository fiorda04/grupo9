package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Tipo;
import com.oo2.grupo9.repositories.TipoRepository;
import com.oo2.grupo9.services.ITipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoService implements ITipoService {

    @Autowired
    private TipoRepository tipoRepository;

    @Override
    public Optional<Tipo> traer(Long id) {
        return tipoRepository.findById(id);
    }

    @Override
    public List<Tipo> traerTodas() {
        return tipoRepository.findAll();
    }
}