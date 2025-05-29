package com.oo2.grupo9.dtos;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UsuarioDTO {
    
    @NotEmpty(message = "El nombre de usuario no puede estar vacio")
    private String nombreUsuario;

    @NotEmpty(message = "La contrasenia no puede estar vacia")
    @Size(min = 6, message = "La contrasenia debe tener al menos 6 caracteres")
    private String contrasenia;

    @NotEmpty(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacio")
    private String apellido;
    
    @NotNull(message = "El DNI no puede estar vacío.")
    private int dni;

    private Long contactoId;
    private Long rolId;
    private boolean activo;

     private List<Long> idsTicketsCliente;
}
