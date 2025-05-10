package com.oo2.grupo9.repositories;

import com.oo2.grupo9.entities.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    List<Localidad> findByNombreLocalidad(String nombreLocalidad);
}
