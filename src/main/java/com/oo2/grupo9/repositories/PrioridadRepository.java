package com.oo2.grupo9.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo9.entities.Prioridad;

@Repository
public interface PrioridadRepository extends JpaRepository<Prioridad, Long> {
	Prioridad findByIdPrioridad(Long idPrioridad);
	
	Prioridad findByNivelPrioridad(int nivelPrioridad);
	
	Prioridad findByNombrePrioridad(String nombrePrioridad);
}
