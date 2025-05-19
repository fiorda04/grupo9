package com.oo2.grupo9.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {

    @NotEmpty(message = "El nombre de usuario no puede estar vacio")
    private String nombreUsuario;

    @NotEmpty(message = "La contraseña no puede estar vacia")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contraseña;

    @NotEmpty(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacio")
    private String apellido;

    @NotEmpty(message = "El dni no puede estar vacio")
    private int dni;

    private Long contactoId;
    private Long rolId;
    private boolean activo;

     private List<Long> idsTicketsCliente;
}
