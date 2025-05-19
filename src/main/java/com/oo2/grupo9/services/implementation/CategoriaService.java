package com.oo2.grupo9.services.implementation;

import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.repositories.CategoriaRepository;
import com.oo2.grupo9.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Categoria traer(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la categoría con ID: " + id));
    }

    @Override
    public List<Categoria> traerTodas() {
        return categoriaRepository.findAll();
    }
}

