package com.oo2.grupo9.dtos;
import java.time.LocalDateTime;

public record CrearUsuarioResponse(
    Long idUsuario,
    String nombre,
    String apellido,
    int dni,
    String nombreUsuario,
    String rol,
    boolean activo,
    LocalDateTime fechaCreacion,
    String email,
    Integer telefono,
    String domicilio,
    String localidad 
) {}