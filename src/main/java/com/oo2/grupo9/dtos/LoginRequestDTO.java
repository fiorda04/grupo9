package com.oo2.grupo9.dtos;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDTO(
    @NotEmpty(message = "El nombre de usuario no puede estar vacío") 
    String username,
    @NotEmpty(message = "La contraseña no puede estar vacía")
    String password
) {}
