package com.oo2.grupo9.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oo2.grupo9.entities.Categoria;
import com.oo2.grupo9.repositories.CategoriaRepository;
import com.oo2.grupo9.services.ICategoriaService;

@Service
public class CategoriaService implements ICategoriaService {
	
	private final CategoriaRepository categoriaRepository;
	
	public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }
	
	@Override
    public List<Categoria> traerTodas() {
        return categoriaRepository.findAll();
    }

}
