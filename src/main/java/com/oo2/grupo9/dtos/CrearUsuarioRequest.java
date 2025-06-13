package com.oo2.grupo9.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearUsuarioRequest(
    
    @NotEmpty(message = "El nombre no puede estar vacío")
    String nombre,
    
    @NotEmpty(message = "El apellido no puede estar vacío")
    String apellido,
    
    @NotNull(message = "El DNI no puede estar vacío")
    Integer dni,
    
    @NotEmpty(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 4, message = "El nombre de usuario debe tener al menos 4 caracteres")
    String nombreUsuario,
    
    @NotEmpty(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String contrasenia,
    
    @NotNull(message = "Se debe especificar un ID de rol")
    Long rolId,

    @NotEmpty(message = "El email no puede estar vacío")
    @Email(message = "Debe ser una dirección de email válida")
    String email,

    @NotNull(message = "El telefono no puede estar vacío")
    Integer telefono,

    @NotEmpty(message = "El domicilio no puede estar vacío")
    String domicilio,
    
    @NotNull(message = "Debes seleccionar una localidad")
    Long localidadId
) {}