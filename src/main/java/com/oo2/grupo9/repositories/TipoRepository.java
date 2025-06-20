package com.oo2.grupo9.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oo2.grupo9.entities.Tipo;

@Repository
public interface TipoRepository extends JpaRepository<Tipo, Long> {
	Tipo findByIdTipo(Long idTipo);
	
	Optional<Tipo> findByNombreTipo(String nombreTipo);
}
