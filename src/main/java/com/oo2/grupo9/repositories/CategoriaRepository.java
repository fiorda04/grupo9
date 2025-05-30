package com.oo2.grupo9.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo9.entities.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	Categoria findByIdCategoria(Long idCategoria);
	
	 Optional<Categoria> findByNombreCategoria(String nombreCategoria);
}