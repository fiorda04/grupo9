package com.oo2.grupo9.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo9.entities.Intervencion;


@Repository
public interface IntervencionRepository extends JpaRepository<Intervencion, Long> {
	Optional<Intervencion> findByIdIntervencion(Long idIntervencion);

	List<Intervencion> findByAutor_IdUsuario(Long idAutor);
	
	
	
}