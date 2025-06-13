package com.oo2.grupo9.services;

import com.oo2.grupo9.dtos.ContactoDTO;
import com.oo2.grupo9.dtos.CrearUsuarioRequest;
import com.oo2.grupo9.dtos.CrearUsuarioResponse;
import com.oo2.grupo9.dtos.UsuarioDTO;
import com.oo2.grupo9.dtos.UsuarioModificacionDTO;
import com.oo2.grupo9.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

    Usuario agregarDesdeDTO(UsuarioDTO usuarioDto, ContactoDTO contactoDto) throws Exception;

    Usuario agregarUsuarioPorAdmin(UsuarioDTO usuarioDto, ContactoDTO contactoDto) throws Exception;

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

    boolean darDeAlta(long idUsuario) throws Exception;

    UsuarioModificacionDTO obtenerUsuarioParaModificar(long idUsuario);

    void actualizarUsuarioAdmin(UsuarioModificacionDTO usuarioModDto) throws Exception;

    List<Usuario> traerPorNombreUsuarioConteniendo(String nombreUsuario);
    
    List<Usuario> traerPorDniExacto(int dni);

    List<Usuario> traerPorEmailConteniendo(String email);

    public CrearUsuarioResponse crearUsuarioRest(CrearUsuarioRequest request) throws Exception;
}
