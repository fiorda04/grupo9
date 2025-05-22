package com.oo2.grupo9.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UsuarioResponseDTO {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String nombreUsuario;
    private String email; 
    private String nombreRol; 
}