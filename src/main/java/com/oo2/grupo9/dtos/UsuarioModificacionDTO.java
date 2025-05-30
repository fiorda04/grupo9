package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data
public class UsuarioModificacionDTO {

    @NotNull(message = "El ID del usuario no puede ser nulo para la modificación.")
    private Long idUsuario;

    @NotEmpty(message = "El nombre de usuario no puede estar vacio")
    private String nombreUsuario;

    // La contraseña es opcional para la modificación
    private String contrasenia; 

    @NotEmpty(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacio")
    private String apellido;
    
    @NotNull(message = "El DNI no puede estar vacío.")
    private Integer dni;

    
    private String email;
    private Integer telefono;
    private String domicilio;
    private Long localidadId; 
    
    @NotNull(message = "El rol no puede estar vacio.")
    private Long rolId; 

}