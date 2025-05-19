package com.oo2.grupo9.services.implementation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oo2.grupo9.entities.Tipo;
import com.oo2.grupo9.repositories.TipoRepository;
import com.oo2.grupo9.services.ITipoService;


@Service
public class TipoService implements ITipoService {
	
	private final TipoRepository tipoRepository;
	
	public TipoService(TipoRepository tipoRepository) {
        this.tipoRepository = tipoRepository;
    }
	
	@Override
    public List<Tipo> traerTodos() {
        return tipoRepository.findAll();
    }

}