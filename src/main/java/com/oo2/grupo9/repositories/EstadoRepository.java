package com.oo2.grupo9.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo9.entities.Estado;


@Repository
public interface EstadoRepository extends JpaRepository<Estado, Long> {
	Estado findByIdEstado(Long idEstado);
	
	 Optional<Estado> findByNombreEstado(String nombreEstado);
}