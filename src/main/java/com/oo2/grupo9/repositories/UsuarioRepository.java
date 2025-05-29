package com.oo2.grupo9.repositories;

import com.oo2.grupo9.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 5. +traer(long idUsuario): Usuario
    Optional<Usuario> findByIdUsuarioAndActivoTrue(Long idUsuario);

    // 6. +traerActivoEInactivo(long idUsuario): Usuario
    Optional<Usuario> findByIdUsuario(Long idUsuario);

    // 7. +traer(String nombreUsuario): Usuario
    Optional<Usuario> findByNombreUsuarioAndActivoTrue(String nombreUsuario);

    // 8. +traerPorDni(int dni): Usuario
    Optional<Usuario> findByDniAndActivoTrue(int dni);

    // 10. +traerPorEmail(String email): Usuario
    Optional<Usuario> findByContacto_EmailAndActivoTrue(String email);

    // 11. +traer(): List<Usuario>
    List<Usuario> findByActivoTrue();

    // 13. +traerPorRol(long idRol): List<Usuario>
    List<Usuario> findByRol_IdRolAndActivoTrue(Long idRol);

    boolean existsByDniAndActivoTrue(int dni);

    boolean existsByContacto_EmailAndActivoTrue(String email);

    Optional<Usuario> findByNombreUsuarioAndIdUsuarioNotAndActivoTrue(String nombreUsuario, Long idUsuario);

    Optional<Usuario> findByContacto_EmailAndContacto_Usuario_IdUsuarioNotAndActivoTrue(String email, Long idUsuario);

    Optional<Usuario> findByDniAndContacto_Usuario_IdUsuarioNotAndActivoTrue(int dni, Long idUsuario);

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    Optional<Usuario> findByDni(int dni);

    Optional<Usuario> findByContacto_Email(String email);
}