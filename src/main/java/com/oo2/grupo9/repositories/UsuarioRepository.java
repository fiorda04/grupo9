package com.oo2.grupo9.repositories;


import com.oo2.grupo9.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    
    Optional<Usuario> findByIdUsuarioAndActivoTrue(Long idUsuario); 
    Optional<Usuario> findByNombreUsuarioAndActivoTrue(String nombreUsuario);
    Optional<Usuario> findByDniAndActivoTrue(int dni);
    Optional<Usuario> findByContacto_EmailAndActivoTrue(String email);
    List<Usuario> findByActivoTrue();
    List<Usuario> findByRol_IdRolAndActivoTrue(Long idRol);
    
    // Para filtros de usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    List<Usuario> findByNombreUsuarioContainingIgnoreCase(String nombreUsuario);
    Optional<Usuario> findByDni(int dni);
    Optional<Usuario> findByContacto_Email(String email);
    List<Usuario> findByContacto_EmailContainingIgnoreCase(String email);
    List<Usuario> findByRol_IdRol(Long idRol); 


    // Para las validaciones en modificar 
    Optional<Usuario> findByNombreUsuarioAndIdUsuarioNotAndActivoTrue(String nombreUsuario, Long idUsuario);
    Optional<Usuario> findByDniAndContacto_Usuario_IdUsuarioNotAndActivoTrue(int dni, Long idUsuario);
    Optional<Usuario> findByContacto_EmailAndContacto_Usuario_IdUsuarioNotAndActivoTrue(String email, Long idUsuario);
    Optional<Usuario> findByIdUsuario(Long idUsuario); 
}