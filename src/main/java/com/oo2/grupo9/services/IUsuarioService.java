package com.oo2.grupo9.services;

import com.oo2.grupo9.dtos.ContactoDTO;
import com.oo2.grupo9.dtos.UsuarioDTO;
import com.oo2.grupo9.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario agregarDesdeDTO(UsuarioDTO usuarioDto, ContactoDTO contactoDto) throws Exception;
    
    void modificar(Usuario usuario);

    void eliminar(Long id); 

    void eliminar(String nombreUsuario);

    Usuario traer(long idUsuario);

    Usuario traerActivoEInactivo(long idUsuario);

    Usuario traer(String nombreUsuario);

    Usuario traerPorDni(int dni);

    Usuario traerPorEmail(String email);

    List<Usuario> traer();

    List<Usuario> traerTodos();

    List<Usuario> traerPorRol(long idRol);
}
