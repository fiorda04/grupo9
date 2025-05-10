package com.oo2.grupo9.dtos;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String email; 
    private String nombreRol; 
}