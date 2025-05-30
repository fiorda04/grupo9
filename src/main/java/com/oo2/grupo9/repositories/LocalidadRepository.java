package com.oo2.grupo9.repositories;

import com.oo2.grupo9.entities.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {
    List<Localidad> findByNombreLocalidad(String nombreLocalidad);

    @Query("SELECT l FROM Localidad l WHERE l.nombreLocalidad = :nombre")
    Optional<Localidad> findByNombreLocalidadonly(@Param("nombre") String nombreLocalidad);
}
