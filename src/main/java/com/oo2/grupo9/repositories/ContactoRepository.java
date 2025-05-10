package com.oo2.grupo9.repositories;


import com.oo2.grupo9.entities.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
    Optional<Contacto> findByEmail(String email);    
}
