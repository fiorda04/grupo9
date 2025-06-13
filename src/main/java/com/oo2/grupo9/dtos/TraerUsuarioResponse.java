package com.oo2.grupo9.dtos;
import java.time.LocalDateTime;

public record TraerUsuarioResponse(
    Long id,
    String nombre,
    String apellido,
    int dni,
    String email,
    String nombreUsuario,
    String rol,
    boolean activo,
    LocalDateTime fechaCreacion
) {}